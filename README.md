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


#### Compilation:
##### Eclipse
1. Download the project files.
2. Import the project into Eclipse.
3. Done!

##### Command Line
TBA

#### Usage:
The Main() function is in the Run.java file.
There, you can change the following variables to modify the NPCs generated:
```Java
public final static int FAMILIES = 5; 
public final static int POPULATION = 20;
public final static int OFFSPRING_MAX = 20;
public final static String NAME_FILEPATH = "Names/Italy";
```

__These will be moved away from the java source code and put into an external config
file for easier editing__

Depending on where you run this, you may need to play around with the `Name` folder
placement. 

#### Features:
Planned
~~Implemented~~

Phase I: Static NPC Generation
* Part A: Biological Systems
  * Simple 
    * ~~Name.~~
    * ~~Age.~~
    * ~~Gender.~~
    * Family Lines. - `50%`
  * Defined
    * Age Distributions.
    * Gender Distributions.
      
*Procedural NPC Generation
  