const admin = require("firebase-admin");
const functions = require("firebase-functions");

admin.initializeApp();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.customToken = functions.https.onRequest((request, response) => {
  functions.logger.info("Hello from custom token", { structuredData: true });
  response.json({ uid: "testsss" });
});
