const admin = require("firebase-admin");
const functions = require("firebase-functions");

admin.initializeApp();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.customToken = functions.https.onRequest((request, response) => {
  functions.logger.info(request.body);
  functions.logger.info("Hello from custom token", { structuredData: true });

  const uid = request.body.data.uid;
  const additionalClaims = { isAdmin: true };

  admin
    .auth()
    .createCustomToken(uid, additionalClaims)
    .then((customToken) => {
      response.json({ data: { customToken } });
    })
    .catch((error) => console.log("Error creating custom token:", error));
});
