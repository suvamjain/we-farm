# we-farm
We Farm is a one stop solution to all problems of farmers in India. Predicts crops using ML Algorithms for better yield. It is an application which comprises of a mobile application as well as a web application. We-Farm gives the farmers/government officials the opportunity to predict crops, to check weather data, to stay in touch with current news and trends, this application also digitalizes the whole process of selling and buying crops. To see detailed report of this project - [click here](https://github.com/suvamjain/we-farm/blob/master/Project%20Report.pdf). Also a [Poster](https://github.com/suvamjain/we-farm/blob/master/Poster.pdf) is available here.

## Modules implemented in android application - 
  - **Crop prediction**:
This module provides the user with the facility to input the data about the different constituents of their field, weather data and regional information to get the best crops that can be grown during that time frame. After getting the input from the user, the data is sent to the server which after running the predictive model on the data returns the different types of crops that can be grown in decreasing order of relevance. The output also provides a video guiding the user about the crop and its important features, these videos are retrieved using YouTube player API. Also, detailed information for better cultivation requirements and insights of the particular crop can be viewed separately.
 - **Weather forecast**:
The user will be notified of the weather change and can also view a detailed analysis of the weather condition in his area for a range of 2 weeks. This data is automatically retrieved using the GPS facility of the device to get the coordinates of the location and passing the same to the OpenWeatherMap API that returns the data for the location in JSON format.
  - **Chat Forum**:
The application has an in-built chat forum in which the user can interact with fellow users, BDO (Block Development Officer) as well as with the representatives of the agricultural community to get their queries cleared as well as provide feedback and suggestions.
  - **Personal Assistant**:
An AI configured chatbot already trained on agriculture related queries and topics is also available to aid the queries of the user. It uses a NLP based model for speech-to-text recognition so that the user ask the queries to the bot and then a text-to-speech model is used to read the result to the user.
  - **E-Commerce**:
To help the farmer from the hassle to go to the mandi and sell their crops in a lower price because of the monopoly of the buyers there, the application has an in-built e-commerce where the farmer can sell their produce at the price decided by the government for that day so that they are not cheated by the buyers. Also, the farmers can buy quality seeds from the e-store based on his needs at government subsidized prices.
  - **News**:
To keep the users up to date with the latest government schemes and news in the agricultural sector the user is notified with the latest news using the TOI news API.

#### Demo [screenshots](https://github.com/suvamjain/we-farm/tree/master/demo_screenshots) can be seen here
