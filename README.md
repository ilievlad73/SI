# SI


## Custom token

The firebase cloud function `custom token` it is used to apply addition claims on the authorization token.
This is needed because in the `rules` section on each service (firestore, storage, database, etc...) we need those claims in order to validate the incoming request.
Notes: From the `rules` context we can't call any firebase service. Only in the firestore rules we access the data from itself.

## Admin
Admins can accept/reject applications and can download any file from the storage section.
Admins can post programs.

## User
User can apply to a program. User can upload files in the `{userUId}/filename.ext` bucket.
