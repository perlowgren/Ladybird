package net.spirangle.ladybird;

public interface Data {
	public static final String APP_NAME        = "ladybird";
	public static final String APP_TITLE       = "Ladybird's Gift";
	public static final String HOST_URL        = "https://ladybird.spirangle.net";

	/** Frames per second. */
	public static final float FPS              = 6.667f;
	/** Seconds per frame. */
	public static float FRAME_RATE             = 1.0f/FPS;

	public static final int GRID               = 10;
	public static final int LAYERS             = 8;

	public static final int START_GAME         = 1;
	public static final int NEXT_LEVEL         = 2;
	public static final int NEXT_ROUND         = 3;

	public static final int SPLASH_NONE        = 0;
	public static final int SPLASH_IDLE        = 1;
	public static final int SPLASH_START       = 2;
	public static final int SPLASH_WIN         = 3;
	public static final int SPLASH_SCORE       = 4;
	public static final int SPLASH_HISCORE     = 5;

	public static final int FONT_PROFONT12B    = 0;
	public static final int FONT_PROFONT12W    = 1;
	public static final int FONT_RISQUE14      = 2;
	public static final int FONT_RISQUE18      = 3;

	public static final int TEXTURE_SPLASH     = 0;
	public static final int TEXTURE_GUI        = 1;
	public static final int TEXTURE_TILES      = 2;
	public static final int TEXTURE_ITEMS      = 3;
	public static final int TEXTURE_CREATURES  = 4;
	public static final int TEXTURE_TROLL      = 5;

	public static final int CLIP_VTILE         = 0;
	public static final int CLIP_HEART         = 1;
	public static final int CLIP_APPLE         = 2;
	public static final int CLIP_CHEST         = 3;
	public static final int CLIP_CHEST_DIM     = 4;
	public static final int CLIP_BOTTLE        = 5;
	public static final int CLIP_BOTTLE_G      = 5;
	public static final int CLIP_BOTTLE_V      = 6;
	public static final int CLIP_BOTTLE_O      = 7;
	public static final int CLIP_BOTTLE_B      = 8;
	public static final int CLIP_LEFT_BUTTON   = 9;
	public static final int CLIP_RIGHT_BUTTON  = 10;
	public static final int CLIP_JUMP_BUTTON   = 11;
	public static final int CLIP_FIRE_BUTTON   = 12;
	public static final int CLIP_GOOGLE_ICON   = 13;

	public static final int SOUND_APPEAR       = 0;
	public static final int SOUND_JUMP         = 1;
	public static final int SOUND_DOOR         = 2;

	public static final int MUSIC_NONE         = -1;
	public static final int MUSIC_SUNNY_DAY    = 0;

	public static final int TILE               = 0;
	public static final int ITEM               = 1;
	public static final int CREATURE           = 2;
	public static final int PLAYER             = 3;
	public static final int PROJECTILE         = 4;

	public static final int HFLAT              = 0;
	public static final int VFLAT              = 1;
	public static final int BLOCK              = 2;
	public static final int BLOCK2             = 3;
	public static final int BLOCK3             = 4;
	public static final int BUSH               = 5;
	public static final int CLOUD              = 6;
	public static final int DOOR               = 7;
	public static final int HEART              = 8;
	public static final int APPLE              = 9;
	public static final int CHEST              = 10;
	public static final int BOTTLE             = 11;
	public static final int BOTTLE_G           = 11;
	public static final int BOTTLE_V           = 12;
	public static final int BOTTLE_O           = 13;
	public static final int BOTTLE_B           = 14;
	public static final int BOOGIE             = 15;
	public static final int GRULL              = 16;
	public static final int TROLL              = 17;

	public static final int LIFE               = 1;
	public static final int AMMO               = 2;
	public static final int POWER              = 3;
	public static final int SPEED              = 4;
	public static final int GOLD               = 5;
	public static final int EXIT               = 6;

	public static final int BUFF               = 0x00000001;
	public static final int AGGRO              = 0x00000002;
	public static final int SOLID              = 0x00000004;
	public static final int MOBILE             = 0x00000008;
	public static final int MOVING             = 0x00000010;
	public static final int DEAD               = 0x00000020;
	public static final int STAND              = 0x00000040;
	public static final int WALK               = 0x00000080;
	public static final int JUMP               = 0x00000100;
	public static final int FLIP               = 0x00000200;
	public static final int HIDDEN             = 0x00000400;
	public static final int PASSIVE            = 0x00000800;

	public static final int LEFT               = 0x00001000;
	public static final int RIGHT              = 0x00002000;
	public static final int UP                 = 0x00004000;
	public static final int DOWN               = 0x00008000;

	public static final int P1                 = 0;        // Player first level configuration
	public static final int P2                 = 1;        // Player facing right configuration
	public static final int P3                 = 2;        // Player facing left configuration
	public static final int C1                 = 0;        // Creature facing right configuration
	public static final int C2                 = 1;        // Creature facing left configuration
	public static final int CL1                = 2;        // Creature moving left configuration
	public static final int CR1                = 3;        // Creature moving right configuration
	public static final int CU1                = 4;        // Creature moving up facing right configuration
	public static final int CU2                = 5;        // Creature moving up facing left configuration
	public static final int CD1                = 6;        // Creature moving down facing right configuration
	public static final int CD2                = 7;        // Creature moving down facing left configuration
	public static final int T1                 = 0;        // Tile configuration
	public static final int TL1                = 1;        // Tile moving left configuration
	public static final int TR1                = 2;        // Tile moving right configuration
	public static final int TU1                = 3;        // Tile moving up configuration
	public static final int TD1                = 4;        // Tile moving down configuration
	public static final int TE1                = 5;        // Exit tile configuration
	public static final int IG1                = 0;        // Gold item configuration
	public static final int BL1                = 1;        // Life buff configuration
	public static final int BA1                = 2;        // Ammo buff configuration
	public static final int BP1                = 3;        // Power buff configuration
	public static final int BS1                = 4;        // Speed buff configuration

	public static int imageIndexByType[] = {
		0,  // Horizontal Flat Tile
		0,  // Vertical Flat Tile
		0,  // Block Tile
		0,  // Large Block 1 Tile
		0,  // Large Block 2 Tile
		0,  // Bush Tile
		0,  // Cloud Tile
		0,  // Door
		1,  // Heart
		1,  // Apple
		1,  // Chest
		1,  // Bottle Green
		1,  // Bottle Violet
		1,  // Bottle Orange
		1,  // Bottle Blue
		2,  // Boogie
		2,  // Grull
		3,  // Troll
	};

	public static int animationIndexByType[][] = {
		{  0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 0, 2, 0, }, // Stand
		{  0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 1, 3, 1, }, // Walk
		{  0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 0, 2, 2, }, // Jump
		{  0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 2, 3, 4, 5, 6, 0, 2, 2, }, // Die
	};
}

