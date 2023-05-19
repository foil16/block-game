package assignment3;

import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		int score = 0;
		Color[][] colors = board.flatten();
		for(int i=0;i<colors.length;i++){
			if(colors[0][i]==targetGoal){
				if(i==0) score+=2;
				else if(i==colors.length-1) score+=2;
				else score+=1;
			}
			if(colors[colors.length-1][i]==targetGoal){
				if(i==0) score+=2;
				else if(i==colors.length-1) score+=2;
				else score+=1;
			}
		}
		for (int i=1;i<colors.length-1;i++){
			if(colors[i][0]==targetGoal){
				if(i==0) score+=2;
				else score+=1;
			}
			if(colors[i][colors.length-1]==targetGoal){
				if(i==0) score+=2;
				else score+=1;
			}
		}





		return score;



	}

	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal) 
		+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
