# TownSim
## NPC generator

#### Description:
Have you ever DMed a game of D&D, thinking that your player characters are grateful,
polite, and respectful? You spend hours upon hours making nations, cities, clans, 
families, and individuals with the utmost care. You go through the tedium of making
logically structured family trees, good enough to trace the heritage of any and every
NPC.

A few hours later, you want to kick it up a notch. Complex relationships are a must
for some great roleplaying. Does Odin Skullcrusher care if Petri de Meriachi's store is
vandalized? Despite being the daughter of Maryanne Mightybush, shouldn't young Marta
inherit the name of her new mother, Brodla Porkmesiter, due to the matrilineal nature
of her society?

Wow! A fortnight has gone by and your society cannot be distinguished from a rich
real-life culture! How your PCs will rejoice! Every action taken by the NPCs will make
perfect sense and all events will deeply impact the artificial community! Surely, your
hard work will make your PCs cry your name out in *The Halls of Magnificent DMs*.


The players burn the city down. 

Everyone is dead.

Only parents who have lost their children know your pain.

	
	
Lament no more! TownSim will allow you to create the same interconnected societies
with many levels of depth in mere seconds. It will give you the tools to simulate rumor
propagation that takes into account each NPC's characteristics (better to commit heinous
crimes in the presence of deafmute orphans, rather than in the constable's office).


### Compilation:
##### Eclipse
1. Download the project files.
2. Import the project into Eclipse.
3. Download JgraphT from http://jgrapht.org/
4. Include the .Jar files you downloaded in your Eclipse project.
5. Run!

##### Command Line
TBA

#### Usage:
The Main() function is in the Run.java file.
The configuration files in the `Configs` folder will generate a population to your specifications:

Genetics.ini sample. 
	FAMILIES: 7					//Number of families to generate.
	POPULATION: 200				//Total number of people to generate.
	MAX_OFFSPRING: 5			//Maximum number of offspring each human can have.
	MIN_REPRODUCTIVE_AGE: 30	//Minimum age difference between a parent/child pair and the minimum age to be eligible to have a child.
	NAME_FILEPATH: SSQCH		//Name modules to use. Can be 'Italy', 'NorthAfrica', 'SouthEast', 'US', and 'SSQCH'.
	MAX_AGE: 80					//Maximum age to create when generating the people.



Depending on where you run this application, you may need to play around with the `Name` and `Configs` folder
placements. 

### Features:
Planned
~~Implemented~~

Phase I: Static NPC Generation - making a snapshot of a populace.

* Part A: Biological Systems
  	* Simple 
    	* ~~Name.~~
    	* ~~Age.~~
    	* ~~Gender.~~
    	* Family Lines. - `70%`
  	* Defined
    	* Age Distributions.
    	* Gender Distributions.
      
* Part B: Social Systems
	* Lineage.
	* Friendship.

	
Phase II: Procedural Populations - make discrete changes to the populace.
	* Birth/Death.
	* Family cluster linking.
	