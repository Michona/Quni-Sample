# DadJokes Quni App


This is a sample app mainly used for demonstrating/showcasing Android architecture and best practices. It's content is intended to be used in an Android course given by developers at Qusion. 

## Features

The main idea is to get a random dad joke from an open api and give option to the user to share it on twitter or save it to his favorites.
Back by server: https://icanhazdadjoke.com/ 

## Architecture 

The app follows the recommendations laid out in the **Guide to App Architecture**. It showcases usage of modern technologies in a simple app. Clean and modern architectural patterns using **Android Architecture Components**. It keeps the logic away from Activities and Fragments and moves it to **ViewModels**, repositories and UseCases. It observes data using **LiveData**. Heavily uses Kotlin **Coroutines** alongside **Apollo** and **GraphQL** for networking solution. It shows proper theming and styling. It relies on **Navigation Component** to simplify into single Activity app. For dependency injection it takes advantage of the simplicity of **Koin**. It uses **MockK** for Unit testing.

## Tech used

* Android Architecture Components (LiveData, ViewModel ..)
* Apollo Client (for GraphQL)
* Koin (dependency injection)
* Navigation Components
* Material Design libs
* MockK (unit tests)
* Fastlane (builds)
* Ktlint (for linting)
* Firebase

### **Disclaimer: The project is still WIP.**
