# Buffup SDK
Integrate the SDK into your Android app and enjoy the buffs!

# TEST REQUIREMENTS & THEIR RESPECTIVE SOLUTIONS
## SDK REQUIREMENTS
**_Expose a function to retrieve the list of available Streams to the host App_**

`BuffApi` is the API wrapper inside **Buffup** library module which exposes all API methods. Inside that, there is a method with `_fun fetchStreams(streamsListFetchedListener: OnStreamsListFetchedListener)_`. It receives the event listener callback i.e. `OnStreamsListFetchedListener`. On API response's error/success, it fires respective event to notify the activity from host app

**_Expose a view that the host App will add in the UI over the video stream to display the Buffs_**

`BuffView` is the Custom View which manages creation, configuration of the whole Buff Controller. Any 3rd party application can show the view simply by adding in their XML like

`
        <com.wajahat.buffup.views.BuffView
            android:id="@+id/buff_view"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentBottom="true"
            app:buff_should_alert_before_ending="true" />
`

**_Handle all the business and UI logic to display the Buffs over the video in the view_**

All the business logic is handled in `BuffView` class inside the library

<br />

## Buff UI
**_The countdown timer should work and at the end if the user hasn't yet voted, the question should automatically hide_**

Please refer to the method `setCountDownTimerView` at **Line # 310** inside **BuffView.kt** class

<br />

**_1 - The number of answers can vary from 2 to 5, your UI should automatically adapt to the number of answers that the API delivers_** <br />
**_2 - If the user selects an answer, the timer should stop and you should hide the Buff after 2 seconds_** 


Please refer to the method `private fun setAnswersView(
        answers: List<StreamDetails.Answer>,
        buffAnswerListener: OnBuffAnswerListener
    )`
  <br /> at **Line # 368** inside **BuffView.kt** class

<br />


***
# EXECUTION
## Tech Stack
* Kotlin - For both Library and the Host app
* Dagger 2 - Used to provide dependency injection
* Retrofit 2 - OkHttp3 - request/response API
* Glide - for image loading
* RxJava 2 - reactive programming paradigm
* LiveData - use LiveData to see UI update with data changes
* Data Binding - bind UI components in layouts to data sources
* JUnit4 - For Unit Testing

## App Architecture
* MVVM - For Host application architecture
* Keep Activity only responsible for UI related code
* ViewModel provides data required by the UI class

## Usage
# Gradle
>
        allprojects {
          repositories {
            maven { url 'https://jitpack.io' }
          }
        }

>
        <com.wajahat.buffup.views.BuffView
            android:id="@+id/buff_view"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentBottom="true"
            app:buff_should_alert_before_ending="true" />

## Attributes

Custom properties to configure  BuffView.

| Name                                         | format        |
| ---------------------------------------------|:-------------:|
| buff_author_info_bg                          | integer       |
| buff_answers_counter_text_bg                 | integer       |
| buff_question_bg                             | integer       |
| buff_answer_row_bg                           | integer       |
| buff_answer_row_index_bg                     | integer       |
| buff_close_icon                              | integer       |
| buff_countdown_timer_alert_duration          | integer       |
| buff_author_info_text_color                  | color         |
| buff_answers_counter_text_color              | color         |
| buff_question_text_color                     | color         |
| buff_answer_row_index_text_color             | color         |
| buff_answer_row_text_color                   | color         |
| buff_should_alert_before_ending              | boolean       |
| buff_countdown_timer_alert_view_zoomed_scale | float         |
