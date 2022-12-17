# OOP Homework 1 - GwentStone
### Implemented by Toaca Alexandra Simona, 322CA

### My approach

- I wanted for the game to be Singleton-like and exist only one instance
of it at a time. That is why the entry point of the actual game is the
static function startGame(), where the internal instance of the game
is initialized. I could have made it a Singleton, but the implementation
was tedious and not worth it.  
- The Game class stores the information for the game, such
as the players, the number of rounds played and the status of the game.  
The Player and GameTable classes are instantiated only within the Game class
and make the commands much easier to implement.  
- The Card class is the blueprint for all the cards and is used for having
a generic Deck and for making the most out of polymorphism. A deck is an array
of Cards, but when the game loads, every card is instantiated as a
certain type of card, for example Sentinel, which is a subclass of
MinionCard.  
- From the Card class derive MinionCard, EnvironmentCard and HeroCard,
each having specific attributes and methods. For example, only a MinionCard
can be frozen, so the methods related to that can only be found in it.
Each of those have subclasses for every type of card, only
some subclasses having special ability methods.  
- The commands for the game have been implemented inside the Game class,
with helper methods from the other classes. The reason is having access to all
the necessary data, such as the current action, current player, etc.
An example would be the command placeCard, which needs
access to both the GameTable instance and the current player.

### What could be improved / My thoughts

- Interfaces could be used to force the implementation of special
abilities in certain cards, but I did not do it because
I did not see any benefit coming from that.
- Every function that has to output something has duplicate code,
but I did not find a satisfying way to fix that
- I forced the Singleton-like implementation of the Game class, but I do not
think it was necessary. I just wanted to use the concept of Singleton.  










