# Project Title
FINDOM-BLOCCHETTI

# Project Description 
This component contains all the necessary bricks to compose a "funding request" for accessing at the European Regional Developement Found
Is it possible to create new funding requests and new bricks

# Configurations
Software parameters and their meaning.
Edit the buildfiles/build.properties to set build information.
Edit the buildfiles/assembler-dev.properties to set the database connection params. This is optional but allows you
to write your funding request directly into the database (using the java\it\csi\findom\blocchetti\business\Assembler2.java class)

# Getting Started 
Clone the component blocchetti by the git repository
You can use the configureg "bonusCultura" funding request or create your own
Create your "funding request" directory under "blocchetti\src\java\it\csi\findom\blocchetti\progetti\presentazione"
Create "funding request" pages under "blocchetti\src\java\it\csi\findom\blocchetti\progetti\presentazione\YOUR_FUNDING_REQUEST"
Compose your pages using blocchetti bricks in "blocchetti\src\java\it\csi\findom\blocchetti\blocchetti"

# Installing 
Build this component and use it as library into the findomwebnew component.

# Deployment 
Run java\it\csi\findom\blocchetti\business\Assembler2.java class to write the funding request into the database.
Create the jar (with ANT: run the distribution task in buil.xml file) and put it into the libs of the findomwebnew component.

# Contributing 
Please read CONTRIBUTING.md for details on our code of conduct, and the process for submitting pull requests to us.

# Versioning 
We use Semantic Versioning for versioning. (http://semver.org)

# Authors 
See the list of contributors who participated in this project in file AUTHORS.txt.

#Copyrights 
See the list of copyrighters in this project in file Copyrights.txt or put here the list of names and years in the form of “© Copyright name – year(s)”.

# License 
Licensed under the EUPL-1.2-or-later. See the LICENSE.txt file for details
