package data;

import static helpers.Artist.*;

public class TileGrid {
	
	public Tile[][] map;
	private int tilesWide, tilesHigh;
	
	public TileGrid()
	{
		this.tilesWide = GAME_WIDTH / TILE_SIZE;//20
		this.tilesHigh = HEIGHT / TILE_SIZE; //15
		map = new Tile[tilesWide][tilesHigh];
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[i].length; j++)
			{
				map[i][j] = new Tile(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE, TileType.Grass);
			}
		}
	}
	
	public TileGrid(int[][] newMap)
	{
		this.tilesWide = newMap[0].length;
		this.tilesHigh = newMap.length;
		map = new Tile[tilesWide][tilesHigh];
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[i].length; j++)
			{	
				switch (newMap[j][i]) {
				case 0:
					map[i][j] = new Tile(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE, TileType.Grass);
					break;
				case 1:
					map[i][j] = new Tile(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE, TileType.Dirt);
					break;
				case 2:
					map[i][j] = new Tile(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE, TileType.Water);
					break;
				case 3:
					map[i][j] = new Tile(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE, TileType.Rock);
					break;

				default:
					System.out.println("Wrong grid number!");
					break;
				}
			}
		}
	}
	
	public void setTile(int xCoord, int yCoord, TileType type)
	{
		map[xCoord][yCoord] = new Tile(xCoord * TILE_SIZE, yCoord * TILE_SIZE, TILE_SIZE, TILE_SIZE, type);
	}
	
	public Tile getTile(int xPlace, int yPlace)
	{
		if(xPlace < tilesWide && yPlace < tilesHigh && xPlace >= 0 && yPlace >= 0)
			return map[xPlace][yPlace];
		else
			return new Tile(0, 0, 0, 0, TileType.NULL);
	}
	
	public void draw()
	{
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[i].length; j++)
			{
				map[i][j].draw();
			}
		}
	}

	public int getTilesWide() {
		return tilesWide;
	}

	public void setTilesWide(int tilesWide) {
		this.tilesWide = tilesWide;
	}

	public int getTilesHigh() {
		return tilesHigh;
	}

	public void setTilesHigh(int tilesHigh) {
		this.tilesHigh = tilesHigh;
	}
}
