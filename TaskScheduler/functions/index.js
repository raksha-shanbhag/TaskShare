const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();
const db = admin.firestore();

// Task Status
const COMPLETE = "COMPLETE";
const DECLINED = "DECLINED";
const OVERDUE = "OVERDUE";
const TRANSFER = "TRANSFER";
const TODO = "TODO";

// Task Cycles
const DAILY = "Daily";
const WEEKLY = "Weekly";
const MONTHLY = "Monthly";
const cyclicTasks = [DAILY, MONTHLY, WEEKLY];

exports.taskScheduler = functions.pubsub.schedule("0 1 * * *").onRun(async () => { // eslint-disable-line
  const currentDate = new Date();
  currentDate.setHours(0, 0, 0, 0);

  // get yesterday
  const yesterday = new Date(currentDate.getDate() - 1);
  yesterday.setHours(0, 0, 0, 0);

  // get subtask Ref
  const subTasksRef = db.collection("SubTasks");
  const snapshot = await subTasksRef.where("endDate", "==", yesterday).get();
  const batch = db.batch();

  snapshot.forEach(async (doc) => {
    const subTask = doc.data();

    // Check if Transfer and change to overdue
    if (subTask.taskStatus !== TRANSFER) {
      batch.update(subTasksRef.doc(doc.id),
          {taskStatus: OVERDUE, taskTransferAssignee: null},
      );
    } else if (subTask.taskStatus !== COMPLETE &&
      subTask.taskStatus !== DECLINED) {
      // Check if status is not COMPLETE or DECLINED, and change to overdue
      batch.update(subTasksRef.doc(doc.id), {taskStatus: OVERDUE});
    }

    // Create new SubTask for completed task
    if (subTask.taskStatus === COMPLETE) {
      const mainTaskRef = db.collection("Tasks").doc(subTask.taskId);
      const mainTaskSnapshot = await mainTaskRef.get();
      const mainTask = mainTaskSnapshot.data();
      const mainTaskEndDate = new Date(mainTask.lastDate);
      const indexStr = mainTask.currentIndex;

      if (mainTask) {
        if (currentDate <= mainTaskEndDate &&
          cyclicTasks.includes(mainTask.cycle)) {
          const newSubTaskEndDate = calculateEnd(mainTask.cycle, currentDate);
          // calculate index
          let newIndex = (isNullOrEmpty(indexStr) ? 0 : parseInt(indexStr)) + 1;

          if (newIndex >= mainTask.assignees.length) {
            newIndex = 0;
          }

          const newSubTask = {
            startDate: currentDate,
            endDate: newSubTaskEndDate,
            taskId: subTask.taskId,
            assigneeId: mainTask.assignees[newIndex],
            taskStatus: TODO,
          };

          batch.update(mainTaskRef, {currentIndex: newIndex});
          batch.set(subTasksRef.doc(), newSubTask);
        }
      }
    }
  });

  await batch.commit();
  return null;
});

/**
 * Calculate end date.
 * @param {string} cycle the task cycle.
 * @param {Date} startDate task start date.
 * @return {Date} task end date.
 */
function calculateEnd(cycle, startDate) {
  if (cycle == MONTHLY) {
    const currentMonth = startDate.getMonth();
    const currentYear = startDate.getFullYear();
    let nextMonth = currentMonth + 1;
    let nextYear = currentYear;

    if (nextMonth > 11) {
      nextMonth = 0; // January
      nextYear++;
    }

    return new Date(nextYear, nextMonth, startDate.getDate());
  } else if (cycle == WEEKLY) {
    const millisecondsInOneDay = 24 * 60 * 60 * 1000;
    return new Date(startDate.getTime() + 7 * millisecondsInOneDay);
  } else {
    return startDate;
  }
}

/**
 * Calculate end date.
 * @param {string} index currentIndex
* @return {Bool} string emptiness.
*/
function isNullOrEmpty(index) {
  return index === null || index === undefined || index === "";
}
