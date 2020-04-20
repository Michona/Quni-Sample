# DadJokes Quni App


This is a sample app mainly used for demostrating/showcasing Android architecture and best practices. It's content is intened to be used in an Android course given by developers at Qusion. 

## Features

The main idea is to get a random dad joke from an open api and give option to the user to share it on twitter or save it to him favorites. 

## Architecture 

The app follows the recommendations laid out in the **Guide to App Architecture**. It showcases usage of modern technologies in a simple app. Clean and modern architectural patterns using **Android Architecuture Components**. It keeps the logic away from Activities and Fragments and moves it to **ViewModels**, repositories and UseCases. It observes data using **LiveData**. It has layered structure of Domain, Presentation and Data layer. Heavily uses Kotlin **Coroutines** alongside **Retrofit** for networking solution. It shows proper theming and styling. It relies on **Navigation Component** to simplify into single Activity app. It uses **Room** for clean offline solution and for saving data on device. For dependency injection it takes advantage of the simplicity of **Koin**.

## Upcoming features

* Write Unit Tests - Using **MockK**
* Add Favourites functionality - Use **Room** to it's extent. 

### **Disclaimer: The project is still WIP.**
