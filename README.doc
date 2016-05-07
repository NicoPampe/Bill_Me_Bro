# Bill_Me_Bro
CSCI 448 Final Project

/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
/*/*    Arcan Lasso Products            /*/*
/*/*    Bill Me Bro                     /*/*
/*/*    Helping You Keep Track of Costs /*/*
/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*

CSCI 448: Mobile Applications - Dr. Hoff, Dr. Paone
Chris Butler, Nico Pampe, John Spielvogel

-------------------------------------------------------------------------------
Bill Me Bro is an application to log who bought what, when, where, how much it cost, and what else is needed. The application should be easy to set up with a low install-to-use turn around time. The app will keeptrack of what the project needs, display receipts once items are purchased. For example, a group of roommates may want to track their household spending in order to ensure that costs are being divided fairly. When someone purchases an item, a picture of the receipt is saved and synced across the group’s devices. Once all the items have been purchased, the total cost and cost-breakdown by item and group member are presented. The Bill Me Bro creates an easy environment to track financial of a project and smooth communications between team members. 

Bill Me Bro requires several key features to be implemented. First, a database is required for the application to use. This database must keep track of team members, items and whether or not they are still needed, receipts, and contributions. After the database and a basic interface are setup, we can work on other features such as sending notifications when the database is updated, and using the camera to take pictures of receipts. A picture acts as documentation towards the cost of the project. Bill Me Bro will utilize touch events to navagate the app and view new groups and receipts. Bill Me Bro will have additional smaller features, but the database, notifications, camera, and touch events are the priority components. 
-------------------------------------------------------------------------------

Bill Me Bro focused on the following features:
    SQLight Database
    Touch events
    Camera
    Custom Views

Our main dificulty was getting the database working correctly. We have tables for the users, groups, and receipts.
In addition, we made all our views fragments to be useful for the activities.

-------------------------------------------------------------------------------


HOW TO RUN THE APP
    simply open the project in Android studios
    let gradle build the project
    Run the project, should build on device

HOW TO NAVIGATE THE APP
    Opening screen shows a username
    Simply put in a username and click Log In
        If a user exsists, the data is pulled from the database.
        Otherwise, a new user is created.

    The next activity shows a list of groups the user is currently in
    Groups can be added and have the following:
        Group Name
        Group Type: description of type.
        Group Members: user can add members in the group.
    Clicking on a group navagates to the receipt list

    In the next activity, two fragments are visable, deepending on the oritation. Both have a menu selection for several features
    Menu items:
        Summarization: opens a dialog of the total amount of the receipts. In adition, contains a pie chart of the break down of the receipts. Pie Chart is a custom view.
        New Receipt: adds a receipt
        Delete Receipt: deletes a receipt
        Remove All Receipts: removes all the receipts from the group
    PORTRAIT:
        A recycle view of the receipts
        Each receipt is grouped by Date (Day, Month, Year)
        Single tap touch event shows a preview
        Swiping left touch event displays an Edit button
            Edit navigates to the ReceiptEditFragment
        Swiping right touch event hides the Edit button
    LANDSCAPE: 
        TwoPane activity with callbacks
        Left is a recyle view of receipts
            A recycle view of the receipts
            Each receipt is grouped by Date (Day, Month, Year)
            Single tap touch event opens the ReceiptEditFragment
        Right is the Edit receipt fragment
            Contains: Title, picture, total, date, contributors, conflicts

    ReceiptEditFragment edits the currently selected receipt.
        Method to take picuture using the Camera.
        Title: editable text field
        Total: onClick -> opens price picker
        Date: onClick -> opens date picker
        Contributors: list of users in the group

--------------------------------------------

////////////
// NOTES  //
////////////
1) There are always three receipts that are added to show multiple date group. They act as an example.

////////////////
// KNOWN BUGS //
////////////////
1) Rotating device from the AddGroupActivity caused the current activity to fail. App is still running.

2) When the database is cleared, you have to back out of the activity to get a refresh.


////////////////////////////
// UNIMPLEMENTED FEATURES //
////////////////////////////
1) Passwords
2) Cloud database
