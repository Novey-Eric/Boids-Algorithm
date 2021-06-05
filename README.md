# Boids-Algorithm
Graphical program implementing boids algorithm.
To run the program, execute the SetupFrame file. By default, there is a GUI that is turned off but it can be enabled by setting the GUI variable in line 20 in SetupFrame to true. The GUI allows you to change the parameters/behavior of the boids.
## Parameters
I will refer to each oval that moves on the screen as a "boid".
Each boid's behavior is controlled by only a few parameters which will each be described below. The main three are the avoidance weight, alignment weight, and center weight.
### radius
If you were to think of a boid as a living organism, the radius paramter would be how far the boid can see around it.
### avoid radius
The avoid radius is the radius in which the boid will begin to turn to avoid other boids. For example if a boid were to be running head on to another, once the two boids get closer than the avoid radius they will begin to turn away from each other.
### avoid weight
The avoid weight determines how strongly the boids will try to avoid hitting each other. This parameter is interesting because at smaller values, the boids tend to congregate into circles.
### align weight
The align weight determines how much the boids want to match velocity vectors. For example, if two boids were traveling 90 degrees offset from each other when they enter the radius of sight, they would match vectors to be at around 45 degrees. Similarly, the velocities also average out in the same fashion.
### center weight
The last weight the boids use is the center weight. The boids tend to be attracted to the "center of mass" of the local group they are flocking in. This weight determines how strongly they do so.
