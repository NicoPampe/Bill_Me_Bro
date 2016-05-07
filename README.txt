# Bill_Me_Bro
CSCI 448 Final Project

/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
/*/*    Arcan Lasso Products            /*/*
/*/*    Bill Me Bro                     /*/*
/*/*    Helping You Keep Track of Costs /*/*
/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*

CSCI 448: Mobile Applications - Dr. Hoff, Dr. Paone
Chris Butler, Nico Pampe, John Spielvogel

-------------------------------------------------------------------------
Bill Me Bro is an application to log who bought what, when, where, and how much it cost. The application should be easy to set up with a low install-to-use turnaround time. The app will keep track of what the project needs. Each user will be able to login by using (due to lack of time) solely their username. At this point the user will be able to create different groups of receipts they wish to keep track of. For instance, a user may want a group type of “personal” to store all personal receipts, or a group type of “school” to track all school related expenses. Once a group has been created the user will see a list of receipts containing three example receipts which portray the way the UI will look when fleshed out with receipts. The list of receipts screen menu contains functionality to clear the entire database excluding the 3 example receipts which are hard coded. Once all the items have been purchased, the total cost and cost-breakdown by item and group member are presented. The Bill Me Bro creates an easy environment to track financial of a project and smooth communications between team members. 

Bill Me Bro requires several key features to be implemented. First, a database is required for the application to use. We intended on using a cloud database, but due to time constraints could not deploy a stable version using this. This would have been a feature we would implement before the next release to ensure our intentional goal of syncing across devices would be met. Therefore, we created a local database This database must keep track of team members, items and whether or not they are still needed, receipts, and contributions. A picture acts as documentation towards the cost of the project. Bill Me Bro will utilize touch events to navigate the app and view new groups and receipts. Bill Me Bro will have additional smaller features, but the database, notifications, camera, and touch events are the priority components. 
-------------------------------------------------------------------------

Bill Me Bro focused on the following features:
    SQLight Database
    Touch events
    Camera
    Custom Views

Our main difficulty was getting the database working correctly. We have tables for the users, groups, and receipts.
In addition, we made all our views fragments to be useful for the activities.

-------------------------------------------------------------------------


HOW TO RUN THE APP
    simply open the project in Android studios
    let gradle build the project
    Run the project, should build on device


=======================
HOW TO NAVIGATE THE APP
=======================

_WELCOME SCREEN_
    Opening screen shows a username
    Simply put in a username and click Log In
        If a user exists, the data is pulled from the database.
        Otherwise, a new user is created.
_LIST OF GROUPS_
    The next activity shows a list of groups the user is currently in
    Groups can be added and have the following:
        Group Name
        Group Type: description of type of group (school, work, etc).
        Group Members: user can add members in the group.

    Clicking on a group navigates to the receipt list

	_EDIT GROUP PAGER_
           Accessible by -> Flinging a group view holder left shows an 
       “EDIT” button.
	    Swipe left or right while in this view to see adjacent groups

_LIST OF RECEIPTS_
    In the next activity, two fragments are visible, depending on the orientation. Both have a menu selection for several features

    MENU ITEMS:
        Summarization: opens a dialog of the total amount of the   
       receipts. In addition, contains a pie chart of the breakdown 
       of the receipts. Pie Chart is a custom view.
        New Receipt: adds a receipt
        Delete Receipt: deletes a receipt
        Remove All Receipts: removes all the receipts from the group
    PORTRAIT:
        An expandable recycle view of the receipts
        Each receipt is grouped by Date (Day, Month, Year)
        Single tap touch event shows a preview
        Swiping left touch event displays an Edit button
            Edit navigates to the ReceiptEditFragment
        Swiping right touch event hides the Edit button
    LANDSCAPE: 
        Two-Pane activity with callbacks
        Left is a recycle view of receipts
            A recycle view of the receipts
            Each receipt is grouped by Date (Day, Month, Year)
            Single tap touch event opens the ReceiptEditFragment
        Right is the Edit receipt fragment
            Contains: Title, picture, total, date, contributors, conflicts

_EDIT RECEIPT PAGER_
    ReceiptEditFragment edits the currently selected receipt.

       Swipe left or right while in this view to see adjacent groups

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

3) (Landscape mode only) If you attempt to collapse an expandable recycler view list item (in the List of Receipts screen) after displaying the detail_fragment view it will crash the application.


////////////////////////////
// UNIMPLEMENTED FEATURES //
////////////////////////////
1) A more secure authentication system including passwords.

2) A cloud database
       a) To sync across multiple devices
       
3) A deeper analysis of receipts (in a group) and analysis of group summary.	

4) List of contributing members (in “Edit Receipt” screen) does not populate from database as intended

5) DO NOT ENTER A BLANK USERNAME, THIS WILL NOT WORK AS INTENDED.


