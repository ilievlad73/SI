const admin = require("firebase-admin");
const functions = require("firebase-functions");

admin.initializeApp({
  serviceAccountId: "firebase-adminsdk-bvr93@si-project-f9475.iam.gserviceaccount.com",
  projectId: "si-project-f9475"
});

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.customToken = functions.https.onRequest((request, response) => {
  functions.logger.info(request.body);
  functions.logger.info("Hello from custom token", { structuredData: true });

  const uid = request.body.data.uid;
  const additionalClaims = { isAdmin: true };

  // if(request.auth.token.uid === uid)

  admin
    .auth()
    .createCustomToken(uid, additionalClaims)
    .then((customToken) => {
      response.json({ data: { customToken } });
    })
    .catch((error) => console.log("Error creating custom token:", error));
});
