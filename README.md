# Profitz

![logo_profitz_black](https://github.com/LionelCainePortfolio/Profitz---android-mobile-app-/assets/152194302/d01c1516-eb8c-424c-8b93-4caae97b77a3)

## Table of Contents

- [General Info](#general-information)
- [Technologies Used](#technologies-used)
- [Features](#features)
  - [Authentication and Registration](#authentication-and-registration)
  - [Promotional Offers](#promotional-offers)
  - [Favorite and Completed Offers](#favorite-and-completed-offers)
  - [User reviews](#user-reviews)
  - [Interactions with listings: commenting, editing, liking and reporting](#interactions-with-listings--commenting--editing--liking-and-reporting)
  - [Licenses for own reflink in offers & Trophies or achievement system](#licenses-for-own-reflink-in-offers)
  - [Airdrops module and cryptocurrency education](#airdrops-module-and-cryptocurrency-education)
  - [Coin system](#coin-system)
  - [Help panel and user support](#help-panel-and-user-support)
  - [Reward and motivation system for users](#reward-and-motivation-system-for-users)
  - [Editing user data](#editing-user-data)
  - [Customizing preferences in the settings tab](#customizing-preferences-in-the-settings-tab)
  - [Advanced private and group chat system](#advanced-private-and-group-chat-system)
  - [User level system](#user-level-system)
  - [Referral Program](#referral-program)
  - [Premium subscription](#premium-subscription)
  - [Bug reporting system](#bug-reporting-system)
  - [User avatar](#user-avatar)
  - [Other basic functions](#other-basic-functions)
- [Screenshots](#screenshots)
- [Room for Improvement](#room-for-improvement)
- [Project Status](#project-status)
- [Acknowledgements](#acknowledgements)
- [Contact](#contact)
<!-- * [License](#license) -->

## General Information

Profitz is an ambitious and the largest project I have worked on in the past two years. It is an advanced educational and informational mobile application in the areas of technology, finance and cryptocurrencies.
Within Profitz, users have the opportunity to create a community, gather information, educate, as well as earn by performing various tasks. The application allows participation in referral programs that guarantee financial benefits, as well as in so-called airdrops, through which users receive cryptocurrencies, later convertible into real money on exchanges. The unique coin collection system allows earning even through active use of the app, participation in chats/forums, creation of blogs or other activities.
<br><br>
Profitz is a significant convenience for anyone interested in the technology, financial and cryptocurrency industries, as well as for those who are just planning to start their business in these areas.  
All in all, Profitz is not just a mobile app; it is a comprehensive ecosystem where users can seamlessly integrate the process of building a community, sharing information and strengthening financial aspects. Thanks to carefully designed features, users not only learn and share their insights on finance and cryptocurrencies, but also actively participate in various tasks, promotions, games and referral programs, earning attractive rewards.

## Technologies Used

Java, Kotlin, PHP, MySQL, Rertorfit 2, Glide, Picasso, RecyclerView, Volley, ViewModel, LiveData, Dagger 2, OkHttp, Push Notifications (Firebase Cloud Messaging), Espresso, Butter Knife

## Features

### Authentication and Registration

<table width="100%">
  <tr>
  <td style="border: none !important;" width="50%">
<img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/signup_login.gif?raw=true" width="400px" />
  </td>
  <td style="border: none !important;" width="50%">
     Profitz's authentication and registration process has been optimized through the use of Firebase Authentication, ensuring a smooth user experience when logging in and creating an account. Users go through a multi-step process where they enter their username, set a password, provide an email address (which is verified in the database), and a phone number (secured with an SMS code). 
       <br><br>
     In addition, users have the option to enter a unique command code. They must also accept the terms and conditions if they want to register and use behind the application. PHP scripts have also been used to implement these functions, which enable connection to the MySQL database and encryption using the SHA algorithm, further strengthening the security of the registration process.
  </td>
  </tr>
</table>

<br>

### Promotional Offers

<table width="100%">
  <tr>
  <td style="border: none !important;" width="50%">
     The function of displaying available promotional offers plays a key role, allowing users to take advantage of various promotions to gain additional benefits. This function uses Volley's tools for optimized communication with the API, allowing users to freely browse available promotional offers. <br><br>
The user interface has been effectively optimized using RecyclerView, which allows promotional offers to be presented clearly. With this integration, users can easily browse and select offers that interest them. In addition, this feature allows users to be redirected to detailed descriptions of the promotions and instructions on how to use the offer to earn the bonus. <br><br>
It should be noted that the instructions consist of two types: textual and visual-textual, which provides comprehensive information to users. The textual instructions detail the steps to take in order to take advantage of the promotion, while the visual-text instructions are enriched with step-by-step screenshots, making the process even easier to understand. <br><br>
The entire feature is based on the efficient use of API and MySQL, which enables real-time data retrieval. This allows users to access the latest information on promotional offers, which increases the attractiveness and usability of the feature. These advanced technological solutions are designed to provide users not only with simple access to promotional offers, but also with efficient and easy-to-understand instructions on how to use them to earn bonuses.

  </td>
  <td style="border: none !important;" width="50%">
<img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/promos.gif?raw=true" width="400px" />
  </td>
  </tr>
</table>

<br>

### Favorite and Completed Offers

<table width="100%">
  <tr>
  <td style="border: none !important;" width="50%">
<img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/favorites_dones.gif?raw=true" width="400px" />
  </td>
  <td style="border: none !important;" width="50%">
     The "Favorite Offers" or "Executed" function allows users to save offers they are interested in, or those they have already executed, in special tabs. It uses the PHP language and a MySQL database to manage the data in the cloud. The application also uses the JSON format for data transfer and the Volley library to improve communication with the API.  <br><br>In addition, the feature uses the Room Persistence library to store data locally, improving access speed and the overall user experience. With this feature, users can easily customize their experience by creating a list of favorite promotions or keeping track of offers they have already made. T <br><br>he simple process of adding offers to the "Favorites" tab allows quick access to favorite offers, greatly enhancing the personalized and rewarding experience of using Profitz.
<br>

  </td>
  </tr>
</table>

### User reviews

<table width="100%">
  <tr>
  <td style="border: none !important;" width="50%">
     Profitz efficiently processes and stores user reviews using features available in PHP, MySQL, JSON and Volley. This dynamic system allows users to comment, edit their comments, like other users' comments and report inappropriate content, helping to create a busy and engaged community around the app. 
 <br><br>
In addition, users have the ability to add reviews to specific listings, and these reviews are displayed when approved by the administrator according to the instructions of the specific listing. This functionality is only available to users who have previously marked a particular promotion as completed. This approach is designed to ensure the authenticity and value of the comments. 
 <br><br>
Users can express their star rating from 1 to 5, as well as add text comments, making their feedback more comprehensive. All this data is stored and managed using PHP, MySQL, JSON and Volley, ensuring not only efficient collection, but also efficient presentation of reviews and comments within the Profitz application. 
 <br><br>
With this feature, Profitz not only becomes a place where users share their experiences, but also creates an interactive community where reviews have a real impact on other users' experiences.
  </td>
  <td style="border: none !important;" width="50%">
         <img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/reviews.gif?raw=true"  width="400px"/>
  </td>
  </tr>
</table>

<br>

### Interactions with listings: commenting, editing, liking and reporting

<table width="100%">
  <tr>
  <td style="border: none !important;" width="50%">
         <img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/comments.gif?raw=true" width="400px"/>
  </td>
  <td style="border: none !important;" width="50%">

In Profitz, users have a wide range of possibilities for interacting with offers, thanks to functionality based on Firebase Cloud Functions and data handling via APIs and MySQL.

<h5>Commenting on Offers:</h5>
Users can freely express their opinions by commenting on specific offers. 
These comments are an integral part of the community, adding value and information to other users. 
<h5>Editing and Deleting Comments:</h5>
Users have control over their comments, and can edit or delete them completely, customizing their experience. 
<h5>Like Comments:</h5>
The likes feature allows users to express their approval of specific comments made by other users. 
This interaction adds a community aspect and strengthens user engagement. 
<h5>Replying to Comments:</h5>
Users have the ability to respond to others' comments, which promotes dialogue and information sharing. 
<h5>Reporting Inappropriate Comments:</h5>
To maintain the level of the community, there is a function for reporting inappropriate comments. 
This tool enables the community to take care of content quality and user safety. 
With these advanced features for interacting with listings, Profitz not only makes it easy for users to share opinions, but also builds an interactive and safe community space around listings.
  </td>
  </tr>
</table>

<br>

### Licenses for own reflink in offers    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Trophies or achievement system 


<table width="100%">
  <tr>
  <td style="border: none !important;" width="50%">

An innovative licence system has been created, allowing users to add their own referral links to offers. As part of this system, there are Licenses that users can purchase with the application's internal currency. These licenses allow users to effectively recommend offers and earn rewards, creating a robust and motivated social network.
<br><br>
Licenses allow users to add their own "Reflinks" to specific offers. Through these reflinks, users have the opportunity to effectively recommend offers to other users and receive compensation for doing so.
<br><br>
The system works by purchasing licenses with the application's internal currency - coins. Users, wishing to add their reflinks, must purchase the corresponding license for "coins", which are available in the application.
<br><br>

<h5>In addition, the system has a complex operation structure:</h5>

Licenses for promotions have priority, and users with a premium account or level 5 affiliate have the option to display their reflink to all users in any promotion for 1 hour once a day.
Users wait until another user's license redemption time has passed if they want to use the same option.
<br><br>
If a premium or level 5 user redeems a license for 1 hour (once a day) and another user redeems it within that time, the first redemption is forfeited.
If a user has referred a friend to Profitz, his friend sees his reflinks in promotions, without having to buy a license with coins. Provided, of course, that this user has added his reflinks.
<br><br>
With these advanced features, the Referral system in Profitz becomes a sophisticated tool that effectively supports community interaction and rewards user activity.

  </td>
  <td style="border: none !important;" width="50%">  
   
  An advanced achievement system that rewards users for achieving certain milestones in the application. Achievements, also known as trophies, are a unique form of motivation, and earning them contributes to a richer user experience in the application. 
<h4>Features of the Achievement System: </h4>

<h5>Rewarding Milestones: </h5>
The achievement system rewards users for achieving specific goals or performing specific actions in the app. 
Each achievement represents a unique distinction and is tied to specific activities in Profitz. 
<h5>Trophies as Collections: </h5>
Achievements take the form of trophies that users can earn and collect. 
Collecting trophies becomes a motivational element that encourages exploration of the application's various features. 
<h5>Displaying Trophies in Profile: </h5>
It is planned to make trophies available in the user's profile, where they will be displayed as visual representations of achievements. 
Displaying earned trophies in the profile adds a visual element to the user experience. 
<h5>Creating Profiles:</h5>
In addition to trophies, the plan is to allow users to create their own profiles. 
The user's profile will become a place to display trophies earned and share them with other users. 
 <br><br>
The achievement system not only rewards users for their activity, but also creates a community where users can share their successes and motivate each other to achieve new goals.
  </td>
  </tr>
</table>

<br>

### Airdrops module and cryptocurrency education

<table width="100%">
  <tr>

  <td style="border: none !important;" width="50%">

Profitz makes it a priority to educate users about airdrops. Before accessing airdrop offerings, users must successfully pass a special quiz that ensures they understand the basics of cryptocurrencies and the risks involved.

<h5>Access to Airdrops:</h5>
Displaying available airdrops where users can earn cryptocurrencies for completing certain tasks. 
Redirection to a detailed description and instructions explaining how to get rewards from an airdrop, provided via API and MySQL. 
<h5>Adding Custom Airdrops:</h5>
The ability to add your own airdrop through a special form. 
Users have active participation in the airdrop community, adding interactivity to the platform. 
<h5>Education Before Airdrops:</h5>
Before airdrops tab is available, users must go through a special introduction on the basics of cryptocurrencies. 
The user must then pass a short quiz to test their knowledge. 
<h5>Airdrops Access Conditions:</h5>
If the user does not pass the quiz, he has the opportunity to approach it again. 
However, until he passes the quiz, he does not have access to the "Airdrops" tab and cannot participate in or add airdrops. 
 <br><br>
This comprehensive module not only allows users to use airdrops, but also emphasizes cryptocurrency education, protecting them from potential risks. This approach increases users' awareness and supports their safe participation in airdrops.

  </td>
    <td style="border: none !important;" width="50%">
               <img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/airdrops.gif?raw=true"  width="400px"/>
  </td>
  </tr>
</table>

<br>

### Coin system

<table width="100%">
  <tr>
      <td style="border: none !important;" width="50%">
               <img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/coins.gif?raw=true"  width="400px"/>

        
  </td>
  <td style="border: none !important;" width="50%">

A comprehensive coin system has been created that not only rewards user activity, but also offers a range of ways to use the points accumulated. This advanced system is designed to encourage users to use the app regularly, emphasizing the variety of coin-generating activities.

<h5>Earning Coins:</h5>
Users earn coins through a variety of activities, such as playing games, watching ads, participating in promotions and airdrops, being active on forums, creating articles, contributing to groups, and more. 
Active use of the app translates into regular awarding of coins, which strengthens community involvement. 
<h5>Topping Up Coins:</h5>
Users have the option to recharge their coin account by purchasing through syncing with PayPal and Hotpay. 
This facility allows flexible management of available funds within the app. 
<h5>Exchanging Coins for Rewards:</h5>
Earned coins are not just an abstract currency, users have the option to exchange them for attractive rewards available in the app. 
This feature further motivates users to actively participate. 
<h5>Sending Coins to Other Users:</h5>
Users can send collected coins to other users, which creates dynamic relationships between community members. 
This option allows users to share resources with other users. 
<h5>Points History:</h5>
This section contains a complete history on points, including exchanging, sending, purchasing, etc. 
Users can track their achievements and transaction history related to points. 
    <br><br>
This comprehensive coin system in Profitz not only motivates users to actively participate, but also creates an attractive environment to support various forms of community involvement.
Earned coins are not just an abstract currency, users have the option to exchange them for attractive rewards available in the app. 
This feature further motivates users to actively participate.

  </td>

  </tr>
</table>

<br>

### Help panel and user support

<table width="100%">
  <tr>

  <td style="border: none !important;" width="50%">

A help panel has also been created to provide users with support at every stage of using the application. This comprehensive panel offers tutorials, frequently asked questions (FAQs) and the ability to chat in real time with support representatives. This supportive and educational environment aims to improve the user experience.

<h5>Tutorials and Frequently Asked Questions (FAQs): </h5>
The help panel contains comprehensive tutorials that take users step-by-step through the use of various application features. 
The FAQ section provides quick answers to frequently asked questions, eliminating the need for lengthy searches for information. 
<h5>Interactive Tabbed Help Panel: </h5>
In most of the app's tabs, users will find a question mark icon in the upper right corner. 
Clicking on this icon launches an interactive help panel, offering three options to choose from: "Show Guide," "Help Section" and "Chat with Consultant." 
<h5>Show Guide: </h5>
"Show Guide" is an option that presents a step-by-step mini-introduction to using a particular tab. 
Users can use this option to quickly familiarize themselves with the functionalities of each activity. 
<h5>Help Section: </h5>
The "Help Section" option redirects users to a tab with articles containing detailed information. 
This place is dedicated to more elaborate descriptions that can be useful in more advanced situations. 
<h5>Chat with Consultant:</h5>
"Chat with Consultant" is an option for instant contact with the support team. 
Users can ask questions and get help in real time, ensuring a quick response to their needs.

<br><br>
This comprehensive help panel is designed not only to provide information, but also to create an interactive environment that supports and educates application users.

  </td>
       <td style="border: none !important;" width="50%">
                  <img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/help_center.gif?raw=true"  width="400px"/>
 </td>
  </tr>
</table>

<br>

### Reward and motivation system for users

<table width="100%">
  <tr>
  <td style="border: none !important;" width="50%">
             <img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/rewards.gif?raw=true"  width="400px"/>
  </td>
  <td style="border: none !important;" width="50%">
     
Supporting user activity, a reward system has been introduced to further motivate users to complete tasks. Users have a number of opportunities to earn additional coins through a variety of app-related activities, creating a dynamic incentive environment.

<h5>Earning Coins for Daily Logins and Activities:</h5>
Profitz rewards users for logging into the app every day, as well as for various app-related activities. 
This approach is designed to keep users consistently engaged. 
<h5>Points for Completing Tasks in the Rewards Tab:</h5>
Users can earn points by completing tasks available in the rewards tab. 
In addition, launching the app daily and receiving a "daily gift" earns users a random amount of coins, depending on how long they have been active in the app. 
<h5>Daily Login Streak:</h5>
The longer a user's daily login streak, the more coins they receive as a gift each day. 
This motivates users to use the app systematically, reinforcing their commitment. 
<h5>History of Earned Coin Rewards:</h5>
This section contains the user's complete history of earned coin rewards. 
Users can keep track of their achievements and history of earned rewards, further motivating them to stay active.

<br><br>
This reward system not only enriches the user experience, but also shapes an interactive environment where engagement translates into real benefits.

  </td>
  </tr>
</table>

<br>

### Editing user data

<table width="100%">
  <tr>
  <td style="border: none !important;" width="50%">
Based on EU directives, the system has also been programmed to give users full control over their profile data, allowing them to edit some information themselves directly in the application. This flexibility allows you to customize and update your profile according to your preferences.

<h5>Self-Editing Data:</h5>
Each user has the ability to edit some of their profile information on their own directly through the app. 
This is a simple and convenient solution that allows you to customize basic data according to your current needs. 
<h5>Data Requiring Contact with the Administrator:</h5>
For certain data, the user will need to contact the application administrator for editing. 
This procedure safeguards certain information, ensuring that changes are authorized and secure. 
<h5>Flexibility in Profile Management:</h5>
Users have the flexibility to manage their profile, deciding which information they want to edit on their own and which requires administrator intervention. 
This approach increases users' control over their data. 
 <br><br>
With this system, users have full control over their profile while ensuring security and authorization for specific information. This solution provides convenience and customization for each user's individual preferences.

  </td>
  <td style="border: none !important;" width="50%">
         <img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/data_edit.gif?raw=true" width="400px"/>
  </td>
  </tr>
</table>

<br>

### Customizing preferences in the settings tab

<table width="100%">
  <tr>
  <td style="border: none !important;" width="50%">
             <img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/settings.gif?raw=true" width="400px"/>
  </td>
  <td style="border: none !important;" width="50%">
     The settings tab is a tab that allows you to personalize various aspects of using the app to better suit your individual expectations.

<h5>Default Settings:</h5>
The settings tab allows users to select default currencies, language, notification preferences and much more. 
This is a central place for customizing the app to suit individual tastes. 
<h5>Personalize Promotional Offers:</h5>
Users have control over selecting the default currency that appears with promotional offers. 
This allows users to conveniently browse promotions tailored to their preferred currency. 
<h5>Language Selection and Data Hiding:</h5>
In the settings tab, users can select the language and make decisions about the visibility of their profile. 
Options include hiding coin balances, comments, reviews and hiding the profile completely. 
<h5>Managing Notifications:</h5>
Users can customize notification settings, deciding which information they want to receive. 
 <br><br>
This flexibility allows users to customize the frequency and type of notifications to suit their preferences. 
With the settings tab, users can personalize their Profitz experience, which contributes to a more convenient and personalized use of the app.

  </td>
  </tr>
</table>

<br>

### Advanced private and group chat system

<table width="100%">
  <tr>
  <td style="border: none !important;" width="50%">
Profitz facilitates user communication with an advanced chat system that offers both private and group options. This innovative system enables the creation of interactive communication spaces tailored to individual preferences.

<h5>Private and Group Chats:</h5>
Profitz provides private and public group functionality that allows users to communicate with other community members. 
Users can create private chats, invite friends and manage group settings. 
<h5>Special Chat System:</h5>
The application has a special chat system that is designed to make communication between users easier and more efficient. 
This system provides powerful tools for chatting, managing groups and integrating with other features of the application. 
<h5>Creating Custom Groups:</h5> 
Users have the ability to create their own groups, both public and private (available only to premium users, more on that below). 
Adding friend referrals to the chat allows users to have private conversations and manage group settings. 
<h5>Group Management:</h5>
Group management features include creating, deleting, renaming, modifying group graphics/avatars, sending messages, and adding/removing group members. 
These capabilities are available through integration with APIs, Node.js and JSON format to ensure efficient system operation. 
 <br><br>
This advanced chat system not only facilitates communication, but also enables a personalized user experience, emphasizing engagement and interaction within the application community.

  </td>
  <td style="border: none !important;" width="50%">
         <img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/chats.gif?raw=true"   width="400px"/>
  </td>
  </tr>
</table>
<br>

### User level system

<table width="100%">
  <tr>
  <td style="border: none !important;" width="50%">
         <img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/level_system.gif?raw=true"  width="400px"/>
  </td>
  <td style="border: none !important;" width="50%">
A five-level system that rewards users with a variety of benefits at each stage of advancement. Users earn higher levels by actively using the app, participating in various activities and earning rewards.

<h4>Five Levels of Advancement:</h4>
There are 5 user levels that are achieved through sustained and active use of the app. 
Each level brings a variety of benefits and rewards to the user, increasing the appeal of using Profitz. 
Benefits at Individual Levels:

<h5>New Partner: </h5>
   <ul>
      <li>Ready-to-use instructions.</li>
      <li>Daily bonus points.</li>
      <li>Daily tasks.</li>
      <li>Receives 0.1% from the promotion.</li>
   </ul>
   
<h5>Level 1 Partner: </h5>
   <ul>
      <li>Earns 10% from the promotion.</li>
      <li>Participates in the affiliate program.</li>
      <li>Receives bonus energy in games.</li>
   </ul>
<h5>Level 2 Partner: </h5>
   <ul>
     <li>Gains 20% from the promotion.</li>
     <li>Bonus energy in games.</li>
   </ul>
   
<h5>Level 3 Partner: </h5>
   <ul>
      <li>Gains 30% from the promotion.</li>
      <li>Receives a free spin in the wheel of fortune.</li>
      <li>Receives the "Cryptocurrency Transfers" guide worth 59 PLN.</li>
      <li>Bonus energy in games.</li>
   </ul>

<h5>Level 4 Partner: </h5>
   <ul>
      <li>Gains 40% from the promotion.</li>
      <li>Receives additional points for recommending the app (15 instead of 10).</li>
      <li>Premium discount -10%.</li>
      <li>Bonus energy in games.</li>
      <li>Three free spins in the wheel of fortune.</li>
   </ul>

<h5>Level 5 Partner:</h5>
   <ul>
      <li>Gains 50% off the promotion.</li>
      <li>Extra points for recommending the app (20 instead of 10).</li>
      <li>Free referral licenses for your partners.</li>
      <li>Premium discount -15%.</li>
      <li>Bonus energy in games.</li>
      <li>Free spin once a day in the wheel of fortune.</li>
   </ul>

This level system creates a dynamic environment, motivating users to stay engaged and gain more benefits as they advance.

  </td>
  </tr>
</table>

<br>

### Referral Program

<table width="100%">
  <tr>
  <td width="50%" style="border: none !important;">
     Profitz is introducing an innovative Referral Program that becomes available to users upon reaching the first level, or upon activation of the premium package. This program not only rewards for active application referrals, but also fosters collaboration within the user community. 
     <h4>Features of the Referral Program: </h4>
<h5>Access to Exclusive Airdrops:</h5>
Premium subscribers have priority access to exclusive airdrops, increasing their chances of earning valuable cryptocurrencies.

<h5>Access to Link and Referral Code:</h5>
Upon reaching the first level, users gain access to a unique link and command code. 
These tools enable them to effectively recommend the app to friends. 
<h5>Fostering Community and Collaboration:</h5>
The Referral Program aims to foster community and cooperation among users. 
Active referrals contribute to the development of the Profitz community. 
<h5>Conditions of Participation: </h5>
Users who have not reached the first level do not have the opportunity to recommend the app to other friends. 
Reaching the first level opens access to the referral function. 
<h5>Special Link and Referral Code:</h5>
Upon reaching level 1, users receive their special link and referral code. 
These unique identifiers allow you to register a new user as a referral. 
<h5>Rewards for Referrer and Referral:</h5>
The referrer and new user receive point rewards and license benefits. 
This reinforces the incentive to actively participate in the Referral Program. 
 <br><br>
The Referral Program at Profitz not only provides an opportunity to share the application, but also builds a strong and motivated community, promoting mutual benefit for users.

  </td>
    <td style="border: none !important;" width="50%">
               <img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/refferal_system.gif?raw=true" width="400px"/>
    </td>

  </tr>
</table>

<br>

### Premium subscription

<table width="100%">
  <tr>
      <td width="50%" style="border: none !important;">
               <img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/premium.gif?raw=true" width="400px"/>


      </td>

  <td style="border: none !important;" width="50%">A premium subscription model has also been developed that allows users to access exclusive features and benefits, enhancing the appeal of using the application. Premium subscribers can enjoy a range of privileges that enhance the quality of their Profitz experience.
<br><br>
<h4>Premium Subscription Features:</h4>
<h5>Access to Exclusive Airdrops:</h5>
Premium subscribers have priority access to exclusive airdrops, increasing their chances of earning valuable cryptocurrencies. 
<h5>Free Promotional Licenses:</h5>
Premium subscribers receive licenses to participate in all promotions (except those marked with an asterisk) for all their referrals. 
<h5>Instant Promotion to Partner Level 5:</h5>
Premium subscription guarantees immediate promotion to partner level 5, providing all associated benefits (one time only). 
<h5>Increased Referral Points:</h5>
Premium subscribers earn 30 points for successfully recommending the app to a friend (instead of the standard 10), and up to 50 points for ultra-premium. 
<h5>Unlimited Energy in Games:</h5>
During the premium subscription, users enjoy unlimited energy in games, improving their gameplay experience. 
<h5>Unlocking Hidden Tasks and Trophies:</h5>
Premium subscribers have access to 25 hidden tasks and 25 trophies, while ultra-premium subscribers gain an additional 50 hidden tasks and 50 trophies. 
<h5>Priority Technical Support:</h5>
Premium users get priority technical support, with answers within hours (on business days). Ultrapremium subscribers can expect answers within 5 minutes. 
<h5>Ad-free:</h5>
Premium subscribers enjoy an ad-free environment in the app, enhancing the user experience. Ultrapremium subscription also eliminates ads. 
<h5>Enhanced Restrictions and Privileges:</h5>
Premium and ultrapremium subscribers enjoy increased limits on adding airdrops, creating groups, sending messages, viewing ads, referring users, and other privileges. 
 <br><br>
Premium Subscription is a unique offering that takes the user experience to the next level, providing exclusive benefits available only to premium and ultrapremium subscribers.</td>
  </tr>
</table>

### Bug reporting system

<table width="100%">
  <tr>
  <td style="border: none !important;" width="50%">The bug reporting system is a feature that allows users to report issues quickly and efficiently. When a user shakes their phone, a special popup appears, enabling them to submit a bug report. Here is a description of this feature:
<h5>Shake Detection:</h5> 
The system is programmed to detect phone shakes. When such a shake is identified, a special message appears on the screen.
<h5>Interactive Popup:</h5>  After a shake, an interactive popup appears on the screen, informing the user about the option to report a bug. This dynamic message is easy to notice and captures the user's attention.
<h5>Reporting Form:</h5>  The popup contains a reporting form where users can describe the encountered issue. The form may include fields such as a problem description, steps to reproduce the bug, and an option to attach a screenshot.
<h5>Automatic Information Relay:</h5>  Technical support receives bug report information, enabling a quick response to identified issues. Automatic data relay shortens the time to address reported bugs.
<h5>Report Status:</h5>  After submitting a report, users may receive a confirmation of receipt and can track the status of the resolution or fix. This enhances transparency and provides a positive user experience.
<h5>Product Improvement:</h5> Bug reports from the Bug Reporting System provide valuable insights from the user perspective. This allows for the quick identification and resolution of problems, contributing to the continuous improvement of the Profitz application.
 <br><br>
Through this feature, not only provides a tool for bug reporting but also engages users in the process of enhancing the product, ultimately leading to increased satisfaction with the application.

</td>
  <td style="border: none !important;" width="50%">
    <img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/bug_reporting.gif?raw=true"  width="400px"/>
  </td>
   </tr>
</table>

### User avatar 

<table width="100%">
  <tr>
  <td style="border: none !important;" width="50%">     <img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/avatar.gif?raw=true"  width="400px"/>
</td>
  <td style="border: none !important;" width="50%">The User Avatar feature empowers users to personalize their profiles by uploading their own avatar images. This functionality ensures a dynamic and engaging user experience. Here's how it works:
<h5>Gallery Access Permission:</h5>
When a user decides to change their avatar, Profitz prompts them to grant permission to access their phone's gallery. This step ensures that the application can retrieve images for selection.
<h5>Avatar Change Option:</h5>
Users navigate to their profile settings or a dedicated avatar section within the app.
Within this section, an option to "Change Avatar" or "Upload Profile Picture" is prominently displayed.
<h5>Gallery Selection:</h5>
Upon selecting the avatar change option, users are redirected to their phone's gallery where they can browse through their photos.
<h5>Image Preview:</h5>
Users can preview and select the image they want to set as their avatar. The selected image is displayed on the screen before confirmation.
After finalizing the avatar choice, users confirm the selection. The new avatar is then updated and immediately visible across the app, providing a seamless transition.
<h5>Avatar Gallery:</h5>
The app may also offer a gallery within the user's profile settings, showcasing their avatar history. This allows users to switch back to previous avatars if desired.
<h5>Permission Reminder:</h5>
If a user denies gallery access initially, the app can provide occasional reminders or prompts, encouraging them to personalize their profile by adding an avatar.
<h5>User Privacy:</h5>
Profitz ensures the privacy of users by clearly communicating that the selected avatar is visible to others within the app but doesn't compromise their overall privacy.
  <br><br>
By integrating the User Avatar feature, not only enhances the personalization options for users but also encourages a sense of identity and community within the application. The intuitive process ensures a positive and engaging user experience.</td>
   </tr>
</table>

### Other basic functions

<table width="100%">
  <tr>
  <td style="border: none !important;" width="50%">
In addition to core features, Profitz incorporates several auxiliary functions aimed at optimizing the overall user experience. These functions, while subtle, play a crucial role in ensuring a seamless and efficient application interaction. Here are some examples:
<br><br>
<h5>Internet Connection Check:</h5>
Profitz monitors the user's internet connection status in real-time.
When the app detects a lack of internet connectivity, it promptly notifies the user.
This feature is essential because certain functionalities, such as saving user progress to the database, require an active internet connection.
<h5>Offline Mode Prompt:</h5>
In the absence of an internet connection, Profitz informs users about the necessity of being online to access the application's full features.
Users receive a clear prompt to establish an internet connection, ensuring they are aware of the app's requirements.
<h5>Synchronization Indicators:</h5>
During data synchronization processes, the app provides clear indicators to inform users that their data is being updated.
This helps users understand the background processes, promoting transparency in data handling.
<h5>Error Handling and Notifications:</h5>
Profitz includes robust error-handling mechanisms to address unexpected issues or glitches.
Users receive informative and user-friendly error messages, guiding them on how to resolve or report the problem.
<h5>Device Orientation Adaptation:</h5>
The app adapts to changes in device orientation seamlessly, ensuring a consistent and visually appealing interface whether users are holding their devices vertically or horizontally.
<h5>Smooth Transitions and Animations:</h5>
Subtle yet effective animations and transitions are incorporated to create a visually pleasing and smooth navigation experience.
These animations enhance the aesthetic appeal of the app and contribute to a positive user perception.
<h5>Progressive Loading:</h5>
Instead of presenting users with a blank screen during data retrieval, Profitz employs progressive loading techniques.
Users see incremental updates, providing a sense of progress and reducing the perception of wait times.
<h5>User-Friendly Error Messages:</h5>
When users encounter errors or issues, the app displays concise and actionable error messages.
These messages guide users on how to address the problem, promoting a user-friendly approach to problem-solving.
<h5>Consistent Design Language:</h5>
Profitz adheres to a consistent design language, ensuring that visual elements, color schemes, and typography are uniform across different sections of the application.
Consistency contributes to a cohesive and harmonious user interface.
    <br><br>
By integrating these auxiliary functions, Profitz not only prioritizes user experience but also demonstrates a commitment to user satisfaction by addressing connectivity concerns, providing clear communication, and creating a visually appealing and intuitive interface.</td>
  <td style="border: none !important;" width="50%">
    <img src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/gifs/no_connection.gif?raw=true"  width="400px"/>
  </td>
   </tr>
</table>



## Screenshots

<div style="display: inline-block;">
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975495.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975509.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975520.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975534.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975545.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975558.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975570.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975583.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975595.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975608.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975622.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975636.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975651.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975665.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975680.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975697.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975716.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975732.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975745.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975760.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975779.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975793.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975806.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975823.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975836.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975855.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975888.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975901.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975916.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975932.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975946.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975958.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975981.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323975993.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976007.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976020.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976038.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976052.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976066.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976080.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976096.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976109.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976126.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976142.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976161.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976173.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976186.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976200.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976213.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976226.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976236.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976248.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976259.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976269.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976280.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976293.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976304.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976315.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976325.jpg?raw=true"/>
  <img width="15%" src="https://github.com/LionelCainePortfolio/Profitz---comprehensive-educational-and-holistic-android-mobile-app/blob/main/screenshots/1704323976336.jpg?raw=true"/>
</div>

## Room for Improvement

Room for improvement:

<ul>
 <li>chat system</li>
 <li>notification system</li>
 <li>admin panel</li>
</ul>
 
To do (I was planning to introduce this):

<ul>
<li>Multilangual</li>
<li>chatbot</li>
<li>the opportunity to earn money by watching ads</li>
<li>3 basic games</li>
<li>a license system to promote offers, promotions or airdrops found by the user</li>
<li>activity monitoring system (user logs)</li>
<li>automatic security system (detection of suspicious activity by the user, abuse, etc.)</li>
<li>introduction</li>
<li>articles in the FAQ</li>
<li>full user settings</li>
<li>improved functioning of the chat tab and groups</li>
<li>fix current bugs</li>
<li>introduction of the KYC and AML procedure</li>
<li>introduction of additional security measures (2FA)</li>
<li>introducing it as the main internal currency instead of coins</li>
<li>the ability to deposit and withdraw the most popular cryptocurrencies</li>
<li>the possibility of exchanging cryptocurrencies for fiat currencies</li>
<li>creating a marketplace of virtual items (NFT)</li>
<li>entering the user's activity level (the higher the number, the greater</li>
<li>the amount of coins obtained from various activities)</li>
<li>100 additional tasks in the application</li>
<li>5 extra games</li>
</ul>

## Project Status

The project is _no longer being worked on_.

I have now abandoned work on the project because I believe that my skills, knowledge and experience have reached a level that allows me to get involved in a larger, commercial project where I will be able to work together as part of a programming team. I am looking for stable employment that will provide me with a steady source of income and allow me to develop in the broader field of programming.

## Acknowledgements

As I mentioned at the beginning, this project is the largest one I have had the opportunity to work on so far. This project has allowed me to significantly expand my knowledge and skills, and has strengthened my conviction about my interests. Programming and developing applications for mobile devices for me is not only a job, but also a passion that gives me great joy, so I am glad that I was able to work on this project and share it with the world, because it has significantly contributed to the growth, improvement of skills and progress in my career.

## Contact

Created by [@LionelCainePorftolio](https://github.com/LionelCainePortfolio) - feel free to contact me!
