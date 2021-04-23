# gestionlaboemmaus
This is a little project that I built for a lab in my country February last year. 
It can help the lab to add new patients, new lab exams, new users, doctors, admin and normal users: .
  - normal users can add new patients, print lab exams bills and results.
  - doctors can prescribe exams to patients
  - admins can add exams on the app, set their pricing, normal values and abnormal values
  - admins can also add new users, deactivate or activate users

### Tools used
I used the framework spring boot to create this project: 
 - thymeleaf was used as template engine
 - spring security with form login, to authenticate and register users 
 - jaspersoft studio was used to create the jrxml files for the billing report and exam reports to be downloaded
