package assignment3;

import java.awt.Color;

public class BlobGoal extends Goal{

	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		Color[][] flat = board.flatten();
		boolean[][] visited = new boolean[flat.length][flat.length];
		int size = 0;
		int temp = 0;
		for(int i=0;i<flat.length;i++){
			for(int j=0;j<flat.length;j++){
				temp = undiscoveredBlobSize(i,j,flat,visited);
				if (temp > size) size=temp;
			}
		}
		return size;

	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal) 
		+ " blocks, anywhere within the block";
	}


	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		int size = 0;
		if(unitCells[i][j] == targetGoal && !visited[i][j]){
			visited[i][j] = true;
			size+=1;
			if(i==unitCells.length-1 || visited[i+1][j]){

			}else size+=undiscoveredBlobSize(i+1,j,unitCells,visited);
			if(j==unitCells.length-1 || visited[i][j+1]){

			}else size+=undiscoveredBlobSize(i,j+1,unitCells,visited);
			if(i==0 || visited[i-1][j]){}
			else size+=undiscoveredBlobSize(i-1,j,unitCells,visited);
			if(j==0 || visited[i][j-1]){}
			else size+=undiscoveredBlobSize(i,j-1,unitCells,visited);
		}else return 0;

	return size;
	}

}
