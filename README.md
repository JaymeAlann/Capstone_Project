## Fuzzy-Logic Medieval Chess Game
### A Chess Variation of Medival Warfare Corp Command Structure

The **Corp Command F-L Medieval Chess Game** variant is a corp structure game where each team is broken up into 
three corp each with their own commanders. These armies follow the following rules:
<br/>
>- The **King** and **Bishop** are corp commanders, each with one command authority that can be used each turn.
>- Up to three Corp Command actions may be taken in each turn, one by each Corp. Corp command Actions **ARE NOT** required in any given turn.
>- Command Authority may not be transferred from one commander to another. 
>- The Kings Bishop commands the three pawns left of the Kings Pawn and the Kings Knight.
>- The Queens Bishop commands the three pawns right of the Queens Pawn and the Queens Knight.
>- The King commands the king and queen pawn, the Queen and the two Rooks. 
>- The King may delegat any of its pieces to be commnanded by either Bishop, at any time.
>- When a Bishop is captured, its command pieces revert to the command of the King, but the command authority is lost. 
>- The game ends similar to that of normal chess, When the King is captured. 

### Capturing Pieces:
In this game, the normal implementation of capturing any piece that is attacked is changed to add some uncertainty to each 
action. The Attacking player must roll a dice to determine if a capture is successful. The die roll needed to capture a piece
depends on the combination of the attacking piece  and the defending piece shown in the table below. When the attack is 
successful, the attacking piece will move to the captured piece destination, if the attack is unsuccessful however; the piece
will remain in its original location. 
<br />
<br />
![Capture Table](./src/chess/gui/images/CaptureTable.png)
<br />
<br />
### UX Design:
When designing the user experience, two things were at the top of the list for most important to remember were 
**user centricity** and **user empathy**. Keeping both of these in mind, every aspect of the experience is enhanced with 
a few assumptions:
- The Player is new
- Instructions will possibly be forgotten
- Moves on board can sometimes happen so fast that every detail needs to be logged
- UI details need to match in order to allow seemless interaction. 

A lot of these UI implementations that were made surrounding these assumptions can be seen below:
![UX Gif](./src/chess/gui/images/UX.gif)
<br />
- Game log with full details on every move attempted both from the player and the AI
- Corp Command Utility whether it is available, unavailable, or captured is available not only on the board, but above the 
game log.
  
- Each piece, when selected shows all the possible moves it can make as well as each piece within its corp in **green**
along with its corp commander in **red**
  
- When a corp command action is taken, not only is the action marked unavailable in red, the board pieces become greyed out 
unmovable. 
  
- When piece are attacking, player vs. AI **OR** AI vs. player, the attacking piece is always on the left, and the defending 
piece on the right. 
  
- Dice animation is also included to help give an immersive feeling to the player that an actual dice is being played. 

While these are just a few of the design choices implemented, it shows the lengths taken to make sure the user was fully aware
of what was happening within the game without being overwhelming or too much in your face. Subtle design choices like this 
allow player to not only enjoy the game, but to play longer and more often. 


*Current Version: 1.1.0*


