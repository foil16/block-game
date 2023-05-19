package assignment3;


import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class Block {
 private int xCoord;
 private int yCoord;
 private int size; // height/width of the square
 private int level; // the root (outer most block) is at level 0
 private int maxDepth; 
 private Color color;

 private Block[] children; // {UR, UL, LL, LR}

 public static Random gen = new Random(4);
 
 
 /*
  * These two constructors are here for testing purposes. 
  */

 public Block() {}
 
 public Block(int x, int y, int size, int lvl, int  maxD, Color c, Block[] subBlocks) {
  this.xCoord=x;
  this.yCoord=y;
  this.size=size;
  this.level=lvl;
  this.maxDepth = maxD;
  this.color=c;
  this.children = subBlocks;
 }
 
 

 /*
  * Creates a random block given its level and a max depth. 
  * 
  * xCoord, yCoord, size, and highlighted should not be initialized
  * (i.e. they will all be initialized by default)
  */
 public Block(int lvl, int maxDepth) {

  this.level = lvl;
  this.maxDepth = maxDepth;
  this.xCoord = 0;
  this.yCoord = 0;
  this.size = 0;
  if (lvl>maxDepth) throw new IllegalArgumentException("Illegal level.");
  Block[] subBlocks;
  if (lvl < maxDepth){
   double prob = gen.nextDouble(1);
   if(prob<Math.exp(-0.25 * lvl)){
    Block[] blocks = new Block[4];
    blocks[0] = new Block(lvl+1,maxDepth);
    blocks[1] = new Block(lvl+1,maxDepth);
    blocks[2] = new Block(lvl+1,maxDepth);
    blocks[3] = new Block(lvl+1,maxDepth);
    this.color = null;
    this.children = blocks;
   }else{
    int col = gen.nextInt(4);
    Block[] blocks = new Block[0];
    this.children = blocks;
    this.color = GameColors.BLOCK_COLORS[col];
    //generate integer
   }
  } else {
   int col = gen.nextInt(4);
   Block[] blocks = new Block[0];
   this.children = blocks;
   this.color = GameColors.BLOCK_COLORS[col];
  }




 }


 /*
  * Updates size and position for the block and all of its sub-blocks, while
  * ensuring consistency between the attributes and the relationship of the 
  * blocks. 
  * 
  *  The size is the height and width of the block. (xCoord, yCoord) are the 
  *  coordinates of the top left corner of the block. 
  */
 public void updateSizeAndPosition (int size, int xCoord, int yCoord) {
  if (size <= 0) throw new IllegalArgumentException("Illegal size");
  if ((size % Math.pow(2,maxDepth-level))!=0) throw new IllegalArgumentException("Illegal size");
  this.size = size;
  this.xCoord = xCoord;
  this.yCoord = yCoord;
  if(children.length == 0){
   return;
  }
  for (int i = 0; i<4; i++){
   if (i==0) {
    children[i].updateSizeAndPosition(size/2,xCoord+(size/2),yCoord);
   } else if (i==1) {
    children[i].updateSizeAndPosition(size/2,xCoord,yCoord);
   } else if (i == 2) {
    children[i].updateSizeAndPosition(size/2,xCoord,yCoord+(size/2));

   }else if (i == 3) {
    children[i].updateSizeAndPosition(size/2,xCoord+(size/2),yCoord+(size/2));
   }
  }

 }

 
 /*
  * Returns a List of blocks to be drawn to get a graphical representation of this block.
  * 
  * This includes, for each undivided Block:
  * - one BlockToDraw in the color of the block
  * - another one in the FRAME_COLOR and stroke thickness 3
  * 
  * Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
  *  
  * The order in which the blocks to draw appear in the list does NOT matter.
  */
 public ArrayList<BlockToDraw> getBlocksToDraw() {
   ArrayList<BlockToDraw> list =  new ArrayList();
   if (this.children.length==0){
      list.add(new BlockToDraw(this.color,this.xCoord,this.yCoord,this.size,0));
      list.add(new BlockToDraw(GameColors.FRAME_COLOR,this.xCoord,this.yCoord,this.size,3));
      return list;
   }
   else{
    for (int i=0;i<this.children.length;i++){

     ArrayList temp = this.children[i].getBlocksToDraw();
     for(int j=0;j<temp.size();j++){
      list.add((BlockToDraw) temp.get(j));
     }
    }
   }

  return list;
 }

 /*
  * This method is provided and you should NOT modify it. 
  */
 public BlockToDraw getHighlightedFrame() {
  return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
 }
 
 
 
 /*
  * Return the Block within this Block that includes the given location
  * and is at the given level. If the level specified is lower than 
  * the lowest block at the specified location, then return the block 
  * at the location with the closest level value.
  * 
  * The location is specified by its (x, y) coordinates. The lvl indicates 
  * the level of the desired Block. Note that if a Block includes the location
  * (x, y), and that Block is subdivided, then one of its sub-Blocks will 
  * contain the location (x, y) too. This is why we need lvl to identify 
  * which Block should be returned. 
  * 
  * Input validation: 
  * - this.level <= lvl <= maxDepth (if not throw exception)
  * - if (x,y) is not within this Block, return null.
  */
 public Block getSelectedBlock(int x, int y, int lvl) {
  if (lvl > maxDepth) throw new IllegalArgumentException("Level exceeds max depth");
  if (lvl < 0) throw new IllegalArgumentException("Level exceeds max depth");
  if (lvl == 0) return this;
  if (children.length == 0) return this;
  if (this.level == lvl-1){
    if (this.size/2 <= x && x <= this.size && 0 <= y && y <= this.size/2){
     return this.children[0];
    }else
    if (0 <= x && x <= this.size/2 && 0 <= y && y <= this.size/2){
     return this.children[1];
    }else
    if (0 <= x && x <= this.size/2 && this.size/2 <= y && y <= this.size){
     return this.children[2];
    }else
    if (this.size/2 <= x && x <= this.size && this.size/2 <= y && y <= this.size){
     return this.children[3];
    }

  }
  else {
   if (this.size/2 <= x && x <= this.size && 0 <= y && y <= this.size/2){
      x = x%(this.size/2);
      y = y%(this.size/2);
      return this.children[0].getSelectedBlock(x,y,lvl);
   }else
   if (0 <= x && x <= this.size/2 && 0 <= y && y <= this.size/2){
      x = x%(this.size/2);
      y = y%(this.size/2);
      return this.children[1].getSelectedBlock(x,y,lvl);
   }else
   if (0 <= x && x <= this.size/2 && this.size/2 <= y && y <= this.size){
      x = x%(this.size/2);
      y = y%(this.size/2);
      return this.children[2].getSelectedBlock(x,y,lvl);
   }else
    if (this.size/2 <= x && x <= this.size && this.size/2 <= y && y <= this.size){
      x = x%(this.size/2);
      y = y%(this.size/2);
      return this.children[3].getSelectedBlock(x,y,lvl);
    }



  }
  return null;
 }

 
 

 /*
  * Swaps the child Blocks of this Block. 
  * If input is 1, swap vertically. If 0, swap horizontally. 
  * If this Block has no children, do nothing. The swap 
  * should be propagate, effectively implementing a reflection
  * over the x-axis or over the y-axis.
  * 
  */
 public void reflect(int direction) {
   if(direction ==1){
    if(children.length != 0){
     Block temp = children[0];
     children[0] = children[1];
     children[1] = temp;
     temp = children[2];
     children[2]=children[3];
     children[3]=temp;
     this.updateSizeAndPosition(this.size,this.xCoord,this.yCoord);
     for (int i =0;i<children.length;i++){
      this.children[i].reflect(direction);
     }
    }
   }
   else if(direction == 0){//x
    if (children.length != 0){
     Block temp = children[0];
     children[0] = children[3];
     children[3] = temp;
     temp = children[1];
     children[1]=children[2];
     children[2]=temp;
     this.updateSizeAndPosition(this.size,this.xCoord,this.yCoord);
     for (int i =0; i<children.length;i++){
      this.children[i].reflect(direction);
     }
    }
  }else throw new IllegalArgumentException("Illegal argument");
 }
 

 
 /*
  * Rotate this Block and all its descendants. 
  * If the input is 1, rotate clockwise. If 0, rotate 
  * counterclockwise. If this Block has no children, do nothing.
  */
 public void rotate(int direction) {
  if (direction != 1 && direction != 0) throw new IllegalArgumentException("Illegal argument");

  if(direction == 0){//counterclockwise
   if(children.length != 0){
    Block temp = children[0];
    Block temp1 = children[1];
    Block temp2 = children[2];
    Block temp3 = children[3];
    children[1]=temp;
    children[0]=temp3;
    children[2]=temp1;
    children[3]=temp2;
    this.updateSizeAndPosition(this.size,this.xCoord,this.yCoord);
    for (int i =0;i<children.length;i++){
     this.children[i].rotate(direction);
    }
   }
  } else
   if(direction == 1){//clockwise
    if(children.length != 0){
    Block temp = children[0];
    Block temp1 = children[1];
    Block temp2 = children[2];
    Block temp3 = children[3];
    children[1]=temp2;
    children[0]=temp1;
    children[2]=temp3;
    children[3]=temp;
    this.updateSizeAndPosition(this.size,this.xCoord,this.yCoord);
    for (int i =0;i<children.length;i++){
     this.children[i].rotate(direction);
    }}
   }

 }
 


 /*
  * Smash this Block.
  * 
  * If this Block can be smashed,
  * randomly generate four new children Blocks for it.  
  * (If it already had children Blocks, discard them.)
  * Ensure that the invariants of the Blocks remain satisfied.
  * 
  * A Block can be smashed iff it is not the top-level Block 
  * and it is not already at the level of the maximum depth.
  * 
  * Return True if this Block was smashed and False otherwise.
  * 
  */
 public boolean smash() {
  if(this.level == maxDepth || level == 0) return false;
  else{
   this.children = new Block[4];
   this.children[0] = new Block(level+1,maxDepth);
   this.children[1] = new Block(level+1,maxDepth);
   this.children[2] = new Block(level+1,maxDepth);
   this.children[3] = new Block(level+1,maxDepth);
   this.updateSizeAndPosition(size,xCoord,yCoord);



  }
  return true;
 }
 
 
 /*
  * Return a two-dimensional array representing this Block as rows and columns of unit cells.
  * 
  * Return and array arr where, arr[i] represents the unit cells in row i, 
  * arr[i][j] is the color of unit cell in row i and column j.
  * 
  * arr[0][0] is the color of the unit cell in the upper left corner of this Block.
  */
 public Color[][] flatten() {
  int maxlvl = this.maxDepth - this.level;
  int byfour = ((int) Math.pow(2,maxlvl));
  Color[][] colors = new Color[byfour][byfour];
  if (this.children.length == 0){
   for (int i=0;i<colors.length;i++){
    for (int j=0;j<colors.length;j++){
     colors[i][j] = this.color;
    }
   }
  }// end of base case
  else{
   Color[][] temp1 = children[0].flatten();
   Color[][] temp2 = children[1].flatten();
   Color[][] temp3 = children[2].flatten();
   Color[][] temp4 = children[3].flatten();
   for (int i=0;i<byfour/2;i++){
    for (int j=byfour/2;j<byfour;j++){
       colors[i][j]=temp1[i][j-byfour/2];
      }


   }
   for (int i=0;i<byfour/2;i++){
    for (int j=0;j<byfour/2;j++){
       colors[i][j]=temp2[i][j];
      }


   }
   for (int i=byfour/2;i<byfour;i++){
    for (int j=0;j<byfour/2;j++){
       colors[i][j]=temp3[i-byfour/2][j];
      }
     }


   for (int i=byfour/2;i<byfour;i++){
    for (int j=byfour/2;j<byfour;j++){
       colors[i][j]=temp4[i-byfour/2][j-byfour/2];
      }
     }


  }


   return colors;



 }

 
 
 // These two get methods have been provided. Do NOT modify them. 
 public int getMaxDepth() {
  return this.maxDepth;
 }
 
 public int getLevel() {
  return this.level;
 }


 /*
  * The next 5 methods are needed to get a text representation of a block. 
  * You can use them for debugging. You can modify these methods if you wish.
  */
 public String toString() {
  return String.format("pos=(%d,%d), size=%d, level=%d"
    , this.xCoord, this.yCoord, this.size, this.level);
 }

 public void printBlock() {
  this.printBlockIndented(0);
 }

 private void printBlockIndented(int indentation) {
  String indent = "";
  for (int i=0; i<indentation; i++) {
   indent += "\t";
  }

  if (this.children.length == 0) {
   // it's a leaf. Print the color!
   String colorInfo = GameColors.colorToString(this.color) + ", ";
   System.out.println(indent + colorInfo + this);   
  } else {
   System.out.println(indent + this);
   for (Block b : this.children)
    b.printBlockIndented(indentation + 1);
  }
 }
 
 private static void coloredPrint(String message, Color color) {
  System.out.print(GameColors.colorToANSIColor(color));
  System.out.print(message);
  System.out.print(GameColors.colorToANSIColor(Color.WHITE));
 }

 public void printColoredBlock(){
  Color[][] colorArray = this.flatten();
  for (Color[] colors : colorArray) {
   for (Color value : colors) {
    String colorName = GameColors.colorToString(value).toUpperCase();
    if(colorName.length() == 0){
     colorName = "\u2588";
    }else{
     colorName = colorName.substring(0, 1);
    }
    coloredPrint(colorName, value);
   }
   System.out.println();
  }
 }
 
}
