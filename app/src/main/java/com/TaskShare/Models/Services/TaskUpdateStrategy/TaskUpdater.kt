package com.TaskShare.Models.Services.TaskUpdateStrategy

class TaskUpdater {
    var taskUpdateStrategy: TaskUpdateStrategy = DailyUpdateStrategy()

    constructor(cycle: String)  {
        if (cycle == "Daily"){
            this.taskUpdateStrategy = DailyUpdateStrategy()
        }
        else if (cycle == "Weekly"){
            this.taskUpdateStrategy = WeeklyUpdateStrategy()
        }
        else if (cycle == "Monthly"){
            this.taskUpdateStrategy = MonthlyUpdateStrategy()
        }

    }

}