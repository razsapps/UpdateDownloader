UpdateDownloader
================

The purpose of this project is to allow developers to control updates of their applications and/or specific files.  It will allow you to pick and chose which files to update.

Updating Applications
=====================
The concept behind updating applications is to have two parts.  

1) Downloader - This is the focus area of this project.  This would be launched by your executable that a user downloads.  The purpose of this section of your application would be to download any specific files/application executables needed to run your application.  Ideally, your downloader should never change (but if you chose to you will have to handle the killing/launching of the Downloader application itself).  Once all downloads/updates are complete the downloader would be used to launch your Application's executable.

2) Application - The actual program you want your user to interact with.

Documentation/Examples
======================
I'll hope to add some documentation/examples soon.
