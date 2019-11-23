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