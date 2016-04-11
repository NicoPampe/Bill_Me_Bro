# Bill_Me_Bro
CSCI 448 Final Project

/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
/*/*    Arcan Lasso Products            /*/*
/*/*    Bill Me Bro                     /*/*
/*/*    Helping You Keep Track of Costs /*/*
/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*

CSCI 448: Mobile Applications - Dr. Hoff, Dr. Paone
Chris Butler, Nico Pampe, John Spielvogel

--------------------------------------------
    Bill Me Bro is an application to log who 
bought what, when, where, how much it cost, 
and what else is needed. The application 
should be easy to set up with a low install-
to-use turn around time. The app will keep 
track of what the project needs, display 
receipts once items are purchased. For 
example, a group of roommates may want to 
track their household spending in order to 
ensure that costs are being divided fairly. 
When someone purchases an item, a picture of 
the receipt is saved and synced across the 
group’s devices. Once all the items have 
been purchased, the total cost and cost-
breakdown by item and group member are 
presented. The Bill Me Bro creates an easy 
environment to track financial of a project 
and smooth communications between team members. 
    Bill Me Bro requires several key features 
to be implemented. First, a database is 
required for the application to use. This 
database must keep track of team members, items 
and whether or not they are still needed, receipts, 
and contributions. After the database and a basic 
interface are setup, we can work on other features 
such as sending notifications when the database is 
updated, and using the camera to take pictures of 
receipts. A picture acts as documentation towards 
the cost of the project. 2D graphics will be 
utilized for highlighting sections on the receipt 
can help indicate important sections such as the 
total amount or tax added to purchase. Bill Me Bro 
will utilize touch events to draw on the receipt 
images to draw attention to important parts of the 
receipt. Bill Me Bro will have additional smaller 
features, but the database, notifications, camera, 
and touch events are the priority components. 
--------------------------------------------

////////////////
// KNOWN BUGS //
////////////////
1) DataBase does not actually get writen to.
    Causes the receipt list to never update.
    Receipts don't change.