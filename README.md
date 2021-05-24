# **FASHION DESIGNER APPLICATION**

User will be able create an account, create or view collections, view a catalogue of available fashion items from a 
clothing brand, customize colours/size and organize/save the items into collections associated with their account. 
(*Eventually I want to see if it's possible to display a 3D model representation of the user collections*). 
Application will be used by online shoppers who are interested in buying items from a specific brand or people who want 
to see what some items may look like together. This project is of interest to me because I currently have a minor 
fashion brand called "*VALOR*" and fashion has always interested me. If I succeed with the 3D model display, in the age 
of online shopping, I think it would be very helpful to see how items look together before purchasing.
 
  ## **User Stories**
  ### PHASE 1
  - As a user, I want to be able to create an account and set a password
  - As a user, I want to create a personal collection
  - As a user, I want to add an item to my collection
  - As a user, I want to delete an item from my collection
  - As a user, I want to be able to see the items which are in my collection
  
  ### PHASE 2
  - As a user, I want to be able to save my account (and associated collections) to file
  - As a user, I want to be able to be able to retrieve my account (using password) and continue where I left off
  
  ### PHASE 4
   ##### Task 2
   implemented use of Map interface
   - AccountManager class uses HashMap to store account names and associated Account objects, 
   using account names as keys to retrieve Account object values
   
   ##### Task 3  
   *I apologize, I did some planned refactoring when I read the line "final version of your code". So I fixed some 
    issues in my design instead, while I should have just put them in this section.*
   - Have association between FashionDesigner and AccountManager be the only point of contact between UI and model,
   then make LoginPanel, NewUserPanel and MainMenuFrame make changes to the AccountManager / Account through the
   FashionDesigner class, thus removing the associations between LoginPanel, NewUserPanel and MainMenuFrame
   - By moving the point of control to FashionDesigner, I would be able to access the selected Account (and its 
   Collections) through AccountManager, thus removing the associations between MainMenuFrame and Account, Collection
   - Having the association between MainMenuFrame and FashionDesigner I could also remove the association between
   MainMenuFrame and JsonSaveAccount since I will be able to "save changes" directly from FashionDesigner
   - Remove FrameType enum and just switch frames without the use of the enum. If I was to add functionality
   though, I would use the enum to load different frames like FAQ or ABOUT.
   
   