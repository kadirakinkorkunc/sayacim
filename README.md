# sayacim
Application that counts for holidays in Turkey with a custom reminder module.
I used AlarmManager, NotificationHelper and BroadcastReceiver libraries for this project.


## Screens and components:  

### •	Main Screen(Welcome Screen):
        In this screen we have static items that is pulled from SQLiteDatabase. Those items represents holidays in Turkey in a recycler view.
![alt text](https://github.com/kadirakinkorkunc/sayacim/blob/master/app/src/main/res/drawable/mainscreen.png)
-- 
        These items are expandable. Every holiday has it's own story written in the expandable area with a counter that can count down without any action.
 ![alt text](https://github.com/kadirakinkorkunc/sayacim/blob/master/app/src/main/res/drawable/mainscreenexpanded.png)

### • Custom Reminder Screen:
        Users can trace and add a custom reminder in another activity with two fragments.
![alt text](https://github.com/kadirakinkorkunc/signalRChatProject/blob/master/ngSignalRChatProject/src/assets/reminderscreenexpanded.png)
![alt text](https://github.com/kadirakinkorkunc/signalRChatProject/blob/master/ngSignalRChatProject/src/assets/reminderaddscreen.png)

### • Notifications:
        When countdown hits zero a notification appears in notifications with explanation. 
        This can happen when application is not working in background or even after reboot.
![alt text](https://github.com/kadirakinkorkunc/signalRChatProject/blob/master/ngSignalRChatProject/src/assets/notificationscreen.png)

