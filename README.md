# MedicationTracker
---
###### Team Members

Lucas Carlsen  
TJ Dailey  
Christopher Elbe  

## Storyboard
[Medication Tracker - Interactive](https://invis.io/PA132AJX8ZFE)

![MedicationTrackerPreview](https://user-images.githubusercontent.com/54542601/197643401-6b9decb7-b53a-49d6-bd3f-3b8f7e740ac4.png)


## Functional Requirements

### Requirement 1: Setting an Alarm for Medication
#### SCENARIO
As a user that takes daily medication,
I want to put a daily timer for medication,
So that I can be healthy.
#### DEPENDENCIES
Setting a daily timer for medications is available and accessible

#### EXAMPLES
###### 1.1
**Given** a feed of medication input data  
**When** I provide medication information and time  
**Then** I should receive an alarm/notification at the given time

### Requirement 2: Setting a Recipient for Missed Medication
#### SCENARIO
As a user that takes daily medication,
I want to set a recipent for missed medication alarms,
So that I can be healthy.

#### EXAMPLES
###### 2.1
**Given** a feed of recipient input data  
**When** I provide recipient contact information  
**Then** They should receive a message at the given information

### Requirement 3: Tracking Amount of Medication Remaining
#### SCENARIO
As a user that takes daily medication,
I want to be notified for a low amount of medication,
So that I can be healthy

#### EXAMPLES
###### 3.1
**Given** a feed of medication input data  
**When** I provide medication amount  
**Then** I should receive a notification for the remaining amount
