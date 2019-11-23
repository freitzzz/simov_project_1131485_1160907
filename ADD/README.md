# ipp-ementa

ipp-ementa is a context-aware Android mobile application that enables students and other interested people to consult the available menus on IPP (Instituto Politécnico do Porto) schools canteens. Its main functionality is the consult of the menus itself, yet ipp-ementa also provides other functionalities such as:

- Canteen location navigation guidance
- Nearby canteens notification
- Favourite dishes availability alert
- Offline mode
- Meal payment

## Architecture Design

To architecturaly design the application, ADD (Attribute-Driven Design) methodology will be used. The use of this methodology will enable the development team to gradually design the application through small ADD iterations. The goals of these iterations are described in the roadmap above:

### Roadmap

Taking into account that the application being developed is a greenfield system, the following iterations are proposed:

#### Iteration 1

- Goal: Structure the application (coarse architectural view)

- Description: In this iteration it is intended that the design of the application being developed is settled in a coarse architectural view in order to understand how the application is integrated with other components

- Design Concepts:

  - Select deployment patterns
  - Select externally developed components (e.g. Frameworks, Platform Target)

#### Iteration 2

- Goal: Support primary functionalities (fine-grain architectural view)

- Description: In this iteration it is intended that the design of the functional requirements and restrictions is settled in a fine-grain architectural view

- Design Concepts:

  - Select architectural patterns
  - Select architectural styles
  - Select storyboard
  - Relate externally developed components with application architecture

#### Iteration 3

- Goal: Support quality attribute scenarios and concerns

- Description: In this iteration it is intended that the design of the quality attribute scenarios and concerns is settled

- Design Concepts:

  - Analyze and choose tactics (e.g. Reduce battery usage)
  - Relate architectural and deployment patterns with choosen tactics
  - Relate externally developed components with choosen tactics


### Architectural Drivers

The following tables describe the existing architectural drivers

#### Use Cases

|ID|Use Case|Description|Priority|Difficulty|Appeared in|Partially addressed in|Addressed in|
|--|--------|-----------|--------|----------|-----------|----------------------|------------|
|UC-1|Consult available schools|To consult school canteens it is necessary to consult which schools are available|High|Low|Pre-iteration 1|--|--|
|UC-2|Consult school canteens|To consult canteens menus it is necessary to consult which canteens a school provides|High|Low|Pre-iteration 1|--|--|
|UC-3|Consult canteen menus|To consult menus dishes it is necessary to consult which menus a canteen offers|High|Low|Pre-iteration 1|--|--|
|UC-4|Consult menu dishes|To consult which dishes I can choose to eat it is necessary to consult which dishes are available on a menu|High|Low|Pre-iteration 1|--|--|
|UC-5|View schools location on map|To ease user experience it is necessary to display school location using on a map|High|Medium|Pre-iteration 1|--|--|
|UC-6|View paths to school on map|To ease user experience it is necessary to indicate possible paths to a school on a map|High|High|Pre-iteration 1|--|--|
|UC-7|Purchase dish|To increase user experience it should be possible to pay a dish using the application|High|High|Pre-iteration 1|--|--|
|UC-8|Choose a dish as personal favourite|There should be a mechanism that allows users to mark the dishes that they enjoyed|Medium|Low|Pre-iteration 1|--|--|
|UC-9|Choose to receive app notifications|Users can receive various notifications on their smartphones|Medium|Low|Pre-iteration 1|--|--|
|UC-10|Receive nearby school notifications|If a user is passing by a school that offers canteens, it would be beneficial that the user is notified of this event|High|High|Pre-iteration 1|--|--|
|UC-11|Receive push-notifications about favourite dishes availability|If a user has marked a dish as favourite it would be beneficial to notify him about the existence of this dish when its available on a menu|Medium|Medium|Pre-iteration 1|--|--|
|UC-12|Choose offline mode in app|It should be possible to use the application without the usage of Wifi/Mobile data|Low|Medium|Pre-iteration 1|--|--|
|UC-13|Change application theme|In order to ease user experience it should be possible to differ the look of the app in white and dark themes|Low|Low|Pre-iteration 1|--|--|

#### Constraints

|ID|Constraint|Difficulty|Appeared in|Partially addressed in|Addressed in|
|--|----------|----------|-----------|----------------------|------------|
|CON-1|The daily usage of the application should not consume more than 10% of battery usage|High|Pre-iteration 1|--|--|
|CON-2|The application must be available at least in 95% of existing Android devices|High|Pre-iteration 1|--|--|
|CON-3|The application must be built only using open-source technologies or technologies with no cost|Medium|Pre-iteration 1|--|--|