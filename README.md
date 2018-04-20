# Notes
A simple notes app showcasing the use of new Android libraries.

* [Room](https://developer.android.com/topic/libraries/architecture/room.html) - for data storage
* [Live Data](https://developer.android.com/topic/libraries/architecture/livedata.html) - to expose data to fragments
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel.html) - to handle data related operations
* [PagedLibrary](https://developer.android.com/topic/libraries/architecture/paging.html) - to power a recyclerview

The app is structured with a single activity and two fragments:
1. FeedFragment is first screen. It displays the list of all notes in descending order of latest modification date.
The list has *swipe-to-delete* functionality and a click on an item launches another fragment to edit the note.
2. ComposeFragment is launched through a floating action button from the FeedFragment or a click on one of the notes. It allows the user to construct a new note or edit an existing one.Notes have title, content and can be labeled with one of the predifined colors.
