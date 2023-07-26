/**
 * Import function triggers from their respective submodules:
 *
 * import {onCall} from "firebase-functions/v2/https";
 * import {onDocumentWritten} from "firebase-functions/v2/firestore";
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

import {onDocumentCreated} from "firebase-functions/v2/firestore";

import admin = require("firebase-admin");
admin.initializeApp();

// Start writing functions
// https://firebase.google.com/docs/functions/typescript

exports.notifyActivity = onDocumentCreated("Activities/{activityId}", async (event) => {
    const snapshot = event.data;

    if (!snapshot) {
        console.log("No data associated with the event");
        return;
    }

    const data = snapshot.data();
    const ids = data.affectedUsers as string[];
    const type = data.type as string;
    const details = data.details as string;
    var userTokens: string[] = [];

    for (var i = 0; i < ids.length; i++) {
        const path = "/Users/" + ids[i];
        const snapshot = await admin.firestore().doc(path).get();

        if (!snapshot.exists) {
            console.log("User doesn't exist")
            console.log(path)
        }

        const token = snapshot.get("notifToken") as string | undefined;

        if (token) {
            userTokens.push(token)
        }
    }

    if (userTokens.length > 0) {
        console.log("Sending notification to devices:");

        for (var i = 0; i < userTokens.length; i++) {
            console.log(userTokens[i]);
        }

        admin.messaging().sendEachForMulticast(
            {
                tokens: userTokens,
                notification: {
                    title: type,
                    body: details
                },
                data: {
                    title: details,
                    body: details,
                },
            }
        )
    }
 });
