package net.spirangle.ladybird;

public class LevelFactory implements Data {

	private static final int levels[][][] = {
		{
			{ 0xc8c8ff,   MUSIC_SUNNY_DAY, 200, 250,   1  },
			{   10, 190, 3, PLAYER,   TROLL,   P1   },	// Player
			{   90, 195, 1, ITEM,     CHEST,   IG1  }, {  155,  95, 1, ITEM,     APPLE,   BA1  }, {  100, 195, 1, ITEM,     BOTTLE_O,BP1  }, {  110, 195, 1, ITEM,     BOTTLE_V,BS1  },
			{  135, 185, 2, TILE,     HFLAT,   T1   }, {  150, 170, 2, TILE,     HFLAT,   T1   }, {  100, 145, 2, TILE,     HFLAT,   T1   }, {   75, 120, 2, TILE,     HFLAT,   T1   },
			{   75, 165, 2, TILE,     HFLAT,   T1   }, {  125, 115, 2, TILE,     HFLAT,   T1   }, {  150, 100, 2, TILE,     HFLAT,   T1   }, {  150, 195, 2, TILE,     BLOCK,   T1   },
			{  150, 180, 2, TILE,     BLOCK,   T1   }, {  175, 170, 2, TILE,     BLOCK,   T1   }, {   90, 130, 2, TILE,     BLOCK,   T1   }, {   90, 140, 2, TILE,     BLOCK,   T1   },
			{   90, 150, 2, TILE,     BLOCK,   T1   }, {   90, 160, 2, TILE,     BLOCK,   T1   }, {  160, 195, 4, TILE,     DOOR,    TE1  }, {  120, 190, 6, TILE,     BUSH,    T1   },
			{   60, 125, 6, TILE,     BUSH,    T1   }, {  150, 165, 5, CREATURE, BOOGIE,  CU2  },
			{    0, 200, 2, TILE,     HFLAT,   T1   }, {   25, 200, 2, TILE,     HFLAT,   T1   }, {   50, 200, 2, TILE,     HFLAT,   T1   }, {   75, 200, 2, TILE,     HFLAT,   T1   },
			{  100, 200, 2, TILE,     HFLAT,   T1   }, {  125, 200, 2, TILE,     HFLAT,   T1   }, {  150, 200, 2, TILE,     HFLAT,   T1   }, {  175, 200, 2, TILE,     HFLAT,   T1   },
			{    0, 225, 2, TILE,     BLOCK2,  T1   }, {   25, 225, 2, TILE,     BLOCK2,  T1   }, {   50, 225, 2, TILE,     BLOCK2,  T1   }, {   75, 225, 2, TILE,     BLOCK2,  T1   },
			{  100, 225, 2, TILE,     BLOCK2,  T1   }, {  125, 225, 2, TILE,     BLOCK2,  T1   }, {  150, 225, 2, TILE,     BLOCK2,  T1   }, {  175, 225, 2, TILE,     BLOCK2,  T1   },
			{    0, 250, 2, TILE,     BLOCK3,  T1   }, {   25, 250, 2, TILE,     BLOCK3,  T1   }, {   50, 250, 2, TILE,     BLOCK3,  T1   }, {   75, 250, 2, TILE,     BLOCK3,  T1   },
			{  100, 250, 2, TILE,     BLOCK3,  T1   }, {  125, 250, 2, TILE,     BLOCK3,  T1   }, {  150, 250, 2, TILE,     BLOCK3,  T1   }, {  175, 250, 2, TILE,     BLOCK3,  T1   },
		},{
			{ 0x969664,  MUSIC_NONE, 200, 250,   2  },
			{   10,  25, 3, PLAYER,   TROLL,   P2   },	// Player
			{    0,  35, 2, TILE,     HFLAT,   T1   }, {   25,  35, 2, TILE,     HFLAT,   T1   }, {  100,  90, 2, TILE,     HFLAT,   T1   }, {  125,  90, 2, TILE,     HFLAT,   T1   },
			{  125,  60, 2, TILE,     HFLAT,   T1   }, {  150, 120, 2, TILE,     HFLAT,   T1   }, {    0,  65, 2, TILE,     HFLAT,   T1   }, {   40,  45, 2, TILE,     BLOCK,   T1   },
			{   50,  55, 2, TILE,     BLOCK,   T1   }, {   60,  65, 2, TILE,     BLOCK,   T1   }, {   70,  75, 2, TILE,     BLOCK,   T1   }, {   80,  85, 2, TILE,     BLOCK,   T1   },
			{   90,  85, 2, TILE,     BLOCK,   T1   }, {  100,  85, 2, TILE,     BLOCK,   T1   }, {  150,  65, 2, TILE,     BLOCK,   T1   }, {  150,  75, 2, TILE,     BLOCK,   T1   },
			{  150,  85, 2, TILE,     BLOCK,   T1   }, {  150,  95, 2, TILE,     BLOCK,   T1   }, {  150, 105, 2, TILE,     BLOCK,   T1   }, {  150, 115, 2, TILE,     BLOCK,   T1   },
			{   15,  75, 2, TILE,     BLOCK,   T1   }, {   25,  85, 2, TILE,     BLOCK,   T1   }, {   35,  95, 2, TILE,     BLOCK,   T1   }, {   45, 105, 2, TILE,     BLOCK,   T1   },
			{   55, 115, 2, TILE,     BLOCK,   T1   }, {   65, 125, 2, TILE,     BLOCK,   T1   }, {   75, 135, 2, TILE,     BLOCK,   T1   }, {   85, 145, 2, TILE,     BLOCK,   T1   },
			{   95, 155, 2, TILE,     BLOCK,   T1   }, {  105, 165, 2, TILE,     BLOCK,   T1   }, {  115, 175, 2, TILE,     BLOCK,   T1   }, {  125, 185, 2, TILE,     BLOCK,   T1   },
			{  135, 195, 2, TILE,     BLOCK,   T1   }, {  140,  85, 1, ITEM,     CHEST,   IG1  }, {  190, 195, 1, ITEM,     CHEST,   IG1  }, {    0,  60, 4, TILE,     DOOR,    TE1  },
			{  130,  55, 5, CREATURE, BOOGIE,  CU2  }, {  190, 145, 5, CREATURE, BOOGIE,  CL1  },
			{    0, 200, 2, TILE,     HFLAT,   T1   }, {   25, 200, 2, TILE,     HFLAT,   T1   }, {   50, 200, 2, TILE,     HFLAT,   T1   }, {   75, 200, 2, TILE,     HFLAT,   T1   },
			{  100, 200, 2, TILE,     HFLAT,   T1   }, {  125, 200, 2, TILE,     HFLAT,   T1   }, {  150, 200, 2, TILE,     HFLAT,   T1   }, {  175, 200, 2, TILE,     HFLAT,   T1   },
			{    0, 225, 2, TILE,     BLOCK2,  T1   }, {   25, 225, 2, TILE,     BLOCK2,  T1   }, {   50, 225, 2, TILE,     BLOCK2,  T1   }, {   75, 225, 2, TILE,     BLOCK2,  T1   },
			{  100, 225, 2, TILE,     BLOCK2,  T1   }, {  125, 225, 2, TILE,     BLOCK2,  T1   }, {  150, 225, 2, TILE,     BLOCK2,  T1   }, {  175, 225, 2, TILE,     BLOCK2,  T1   },
			{    0, 250, 2, TILE,     BLOCK3,  T1   }, {   25, 250, 2, TILE,     BLOCK3,  T1   }, {   50, 250, 2, TILE,     BLOCK3,  T1   }, {   75, 250, 2, TILE,     BLOCK3,  T1   },
			{  100, 250, 2, TILE,     BLOCK3,  T1   }, {  125, 250, 2, TILE,     BLOCK3,  T1   }, {  150, 250, 2, TILE,     BLOCK3,  T1   }, {  175, 250, 2, TILE,     BLOCK3,  T1   },
		},{
			{ 0x969664,  MUSIC_NONE, 200, 250,   3  },
			{   50,  60, 3, PLAYER,   TROLL,   P3   },	// Player
			{  170, 175, 1, ITEM,     CHEST,   IG1  }, {  185,  30, 1, ITEM,     CHEST,   IG1  }, {  175, 110, 2, TILE,     HFLAT,   TU1  }, {    0,  30, 2, TILE,     HFLAT,   T1   },
			{   25,  35, 2, TILE,     HFLAT,   T1   }, {   70,  35, 2, TILE,     HFLAT,   T1   }, {   95,  35, 2, TILE,     HFLAT,   T1   }, {  120,  35, 2, TILE,     HFLAT,   T1   },
			{  175,  35, 2, TILE,     HFLAT,   T1   }, {   40,  80, 2, TILE,     HFLAT,   T1   }, {   65,  80, 2, TILE,     HFLAT,   T1   }, {    0, 135, 2, TILE,     HFLAT,   T1   },
			{   25, 135, 2, TILE,     HFLAT,   T1   }, {   40, 165, 2, TILE,     HFLAT,   T1   }, {   65, 165, 2, TILE,     HFLAT,   T1   }, {   80, 110, 2, TILE,     HFLAT,   T1   },
			{   80, 150, 2, TILE,     HFLAT,   T1   }, {  105, 150, 2, TILE,     HFLAT,   T1   }, {  165, 180, 2, TILE,     HFLAT,   T1   }, {   70,  45, 2, TILE,     BLOCK,   T1   },
			{   70,  55, 2, TILE,     BLOCK,   T1   }, {   70,  65, 2, TILE,     BLOCK,   T1   }, {   70,  75, 2, TILE,     BLOCK,   T1   }, {   80,  75, 2, TILE,     BLOCK,   T1   },
			{   90,  80, 2, TILE,     BLOCK,   T1   }, {  135,  45, 2, TILE,     BLOCK,   T1   }, {  145,  55, 2, TILE,     BLOCK,   T1   }, {  155,  65, 2, TILE,     BLOCK,   T1   },
			{  155,  75, 2, TILE,     BLOCK,   T1   }, {  165,  75, 2, TILE,     BLOCK,   T1   }, {  145,  85, 2, TILE,     BLOCK,   T1   }, {   20,  95, 2, TILE,     BLOCK,   T1   },
			{   30,  85, 2, TILE,     BLOCK,   T1   }, {  135,  95, 2, TILE,     BLOCK,   T1   }, {  125,  95, 2, TILE,     BLOCK,   T1   }, {  130, 105, 2, TILE,     BLOCK,   T1   },
			{  130, 115, 2, TILE,     BLOCK,   T1   }, {  130, 125, 2, TILE,     BLOCK,   T1   }, {  130, 135, 2, TILE,     BLOCK,   T1   }, {  130, 145, 2, TILE,     BLOCK,   T1   },
			{  140, 135, 2, TILE,     BLOCK,   T1   }, {   50, 135, 2, TILE,     BLOCK,   T1   }, {   60, 125, 2, TILE,     BLOCK,   T1   }, {   70, 115, 2, TILE,     BLOCK,   T1   },
			{  190,  95, 2, TILE,     BLOCK,   T1   }, {   20, 170, 2, TILE,     BLOCK,   T1   }, {   30, 170, 2, TILE,     BLOCK,   T1   }, {   80, 160, 2, TILE,     BLOCK,   T1   },
			{  100, 195, 2, TILE,     BLOCK,   T1   }, {  110, 195, 2, TILE,     BLOCK,   T1   }, {  120, 195, 2, TILE,     BLOCK,   T1   }, {  190, 180, 2, TILE,     BLOCK,   T1   },
			{   80,  65, 1, ITEM,     APPLE,   BA1  }, {    5, 195, 1, ITEM,     CHEST,   IG1  }, {    0,  25, 4, TILE,     DOOR,    TE1  }, {    0,  75, 5, CREATURE, BOOGIE,  CU1  },
			{  105,  85, 5, CREATURE, BOOGIE,  CU2  }, {  110, 170, 5, CREATURE, BOOGIE,  CL1  },
			{    0, 200, 2, TILE,     HFLAT,   T1   }, {   25, 200, 2, TILE,     HFLAT,   T1   }, {   50, 200, 2, TILE,     HFLAT,   T1   }, {   75, 200, 2, TILE,     HFLAT,   T1   },
			{  100, 200, 2, TILE,     HFLAT,   T1   }, {  125, 200, 2, TILE,     HFLAT,   T1   }, {  150, 200, 2, TILE,     HFLAT,   T1   }, {  175, 200, 2, TILE,     HFLAT,   T1   },
			{    0, 225, 2, TILE,     BLOCK2,  T1   }, {   25, 225, 2, TILE,     BLOCK2,  T1   }, {   50, 225, 2, TILE,     BLOCK2,  T1   }, {   75, 225, 2, TILE,     BLOCK2,  T1   },
			{  100, 225, 2, TILE,     BLOCK2,  T1   }, {  125, 225, 2, TILE,     BLOCK2,  T1   }, {  150, 225, 2, TILE,     BLOCK2,  T1   }, {  175, 225, 2, TILE,     BLOCK2,  T1   },
			{    0, 250, 2, TILE,     BLOCK3,  T1   }, {   25, 250, 2, TILE,     BLOCK3,  T1   }, {   50, 250, 2, TILE,     BLOCK3,  T1   }, {   75, 250, 2, TILE,     BLOCK3,  T1   },
			{  100, 250, 2, TILE,     BLOCK3,  T1   }, {  125, 250, 2, TILE,     BLOCK3,  T1   }, {  150, 250, 2, TILE,     BLOCK3,  T1   }, {  175, 250, 2, TILE,     BLOCK3,  T1   },
		},{
			{ 0xc8c8ff,   MUSIC_SUNNY_DAY, 200, 250,   4  },
			{   10, 185, 3, PLAYER,   TROLL,   P3   },	// Player
			{    0, 195, 2, TILE,     HFLAT,   TU1  }, {    0,  40, 2, TILE,     HFLAT,   T1   }, {   70,  55, 2, TILE,     HFLAT,   T1   }, {  160,  55, 2, TILE,     HFLAT,   T1   },
			{   60,  80, 2, TILE,     HFLAT,   T1   }, {  175,  80, 2, TILE,     HFLAT,   T1   }, {   40, 105, 2, TILE,     HFLAT,   T1   }, {  120,  90, 2, TILE,     HFLAT,   T1   },
			{   55, 120, 2, TILE,     HFLAT,   T1   }, {   80, 120, 2, TILE,     HFLAT,   T1   }, {  135, 135, 2, TILE,     HFLAT,   T1   }, {   55, 165, 2, TILE,     HFLAT,   T1   },
			{  125, 170, 2, TILE,     HFLAT,   T1   }, {  150, 170, 2, TILE,     HFLAT,   T1   }, {  175, 170, 2, TILE,     HFLAT,   T1   }, {   40, 185, 2, TILE,     HFLAT,   T1   },
			{   75,  10, 2, TILE,     BLOCK,   T1   }, {   15,  35, 2, TILE,     BLOCK,   T1   }, {   15,  50, 2, TILE,     BLOCK,   T1   }, {   25,  60, 2, TILE,     BLOCK,   T1   },
			{   75,  65, 2, TILE,     BLOCK,   T1   }, {   75,  75, 2, TILE,     BLOCK,   T1   }, {  175,  65, 2, TILE,     BLOCK,   T1   }, {  175,  75, 2, TILE,     BLOCK,   T1   },
			{   60,  90, 2, TILE,     BLOCK,   T1   }, {   55, 100, 2, TILE,     BLOCK,   T1   }, {   55, 115, 2, TILE,     BLOCK,   T1   }, {  120,  75, 2, TILE,     BLOCK,   T1   },
			{  120,  85, 2, TILE,     BLOCK,   T1   }, {  115, 100, 2, TILE,     BLOCK,   T1   }, {  115, 110, 2, TILE,     BLOCK,   T1   }, {  105, 120, 2, TILE,     BLOCK,   T1   },
			{  190,  90, 2, TILE,     BLOCK,   T1   }, {  190, 100, 2, TILE,     BLOCK,   T1   }, {  135, 145, 2, TILE,     BLOCK,   T1   }, {  125, 155, 2, TILE,     BLOCK,   T1   },
			{  125, 165, 2, TILE,     BLOCK,   T1   }, {  115, 170, 2, TILE,     BLOCK,   T1   }, {  190, 155, 2, TILE,     BLOCK,   T1   }, {  190, 165, 2, TILE,     BLOCK,   T1   },
			{   45, 160, 2, TILE,     BLOCK,   T1   }, {   45, 170, 2, TILE,     BLOCK,   T1   }, {   45, 180, 2, TILE,     BLOCK,   T1   }, {   40, 195, 2, TILE,     BLOCK,   T1   },
			{   90, 185, 2, TILE,     BLOCK,   T1   }, {   90, 195, 2, TILE,     BLOCK,   T1   }, {    0,  35, 1, ITEM,     APPLE,   BA1  }, {  155, 165, 1, ITEM,     BOTTLE_O,BP1  },
			{   85,  50, 1, ITEM,     CHEST,   IG1  }, {  190,  75, 1, ITEM,     CHEST,   IG1  }, {  145, 165, 1, ITEM,     CHEST,   IG1  }, {  185, 195, 1, ITEM,     CHEST,   IG1  },
			{   65, 115, 4, TILE,     DOOR,    TE1  }, {   40,  90, 5, CREATURE, BOOGIE,  CU2  }, {  180,  25, 5, CREATURE, BOOGIE,  CL1  }, {  180, 195, 5, CREATURE, GRULL,   CL1  },
			{   95,  60, 6, TILE,     BUSH,    T1   }, {  145,  60, 6, TILE,     BUSH,    T1   }, {   85,  85, 6, TILE,     BUSH,    T1   }, {  145,  95, 6, TILE,     BUSH,    T1   },
			{   30, 120, 6, TILE,     BUSH,    T1   }, {    0, 140, 6, TILE,     BUSH,    T1   }, {  160, 140, 6, TILE,     BUSH,    T1   }, {   80, 170, 6, TILE,     BUSH,    T1   },
			{   65, 190, 6, TILE,     BUSH,    T1   },
			{    0, 200, 2, TILE,     HFLAT,   T1   }, {   25, 200, 2, TILE,     HFLAT,   T1   }, {   50, 200, 2, TILE,     HFLAT,   T1   }, {   75, 200, 2, TILE,     HFLAT,   T1   },
			{  100, 200, 2, TILE,     HFLAT,   T1   }, {  125, 200, 2, TILE,     HFLAT,   T1   }, {  150, 200, 2, TILE,     HFLAT,   T1   }, {  175, 200, 2, TILE,     HFLAT,   T1   },
			{    0, 225, 2, TILE,     BLOCK2,  T1   }, {   25, 225, 2, TILE,     BLOCK2,  T1   }, {   50, 225, 2, TILE,     BLOCK2,  T1   }, {   75, 225, 2, TILE,     BLOCK2,  T1   },
			{  100, 225, 2, TILE,     BLOCK2,  T1   }, {  125, 225, 2, TILE,     BLOCK2,  T1   }, {  150, 225, 2, TILE,     BLOCK2,  T1   }, {  175, 225, 2, TILE,     BLOCK2,  T1   },
			{    0, 250, 2, TILE,     BLOCK3,  T1   }, {   25, 250, 2, TILE,     BLOCK3,  T1   }, {   50, 250, 2, TILE,     BLOCK3,  T1   }, {   75, 250, 2, TILE,     BLOCK3,  T1   },
			{  100, 250, 2, TILE,     BLOCK3,  T1   }, {  125, 250, 2, TILE,     BLOCK3,  T1   }, {  150, 250, 2, TILE,     BLOCK3,  T1   }, {  175, 250, 2, TILE,     BLOCK3,  T1   },
		},{
			{ 0xc8c8ff,   MUSIC_SUNNY_DAY, 200, 250,   4  },
			{  125, 215, 3, PLAYER,   TROLL,   P3   },	// Player
			{   55,  40, 2, TILE,     HFLAT,   T1   }, {    0,  90, 2, TILE,     HFLAT,   T1   }, {   65,  95, 2, TILE,     HFLAT,   T1   }, {   50, 150, 2, TILE,     HFLAT,   T1   },
			{  115, 145, 2, TILE,     HFLAT,   T1   }, {  100, 175, 2, TILE,     HFLAT,   T1   }, {  125, 175, 2, TILE,     HFLAT,   T1   }, {   20, 200, 2, TILE,     HFLAT,   T1   },
			{   30, 215, 2, TILE,     HFLAT,   T1   }, {   55, 215, 2, TILE,     HFLAT,   T1   }, {  125, 230, 2, TILE,     HFLAT,   T1   }, {  150, 220, 2, TILE,     HFLAT,   T1   },
			{  175, 130, 2, TILE,     HFLAT,   T1   }, {   70,  50, 2, TILE,     BLOCK,   T1   }, {   70,  60, 2, TILE,     BLOCK,   T1   }, {   70,  70, 2, TILE,     BLOCK,   T1   },
			{   70,  80, 2, TILE,     BLOCK,   T1   }, {   70,  90, 2, TILE,     BLOCK,   T1   }, {    0, 100, 2, TILE,     BLOCK,   T1   }, {    0, 110, 2, TILE,     BLOCK,   T1   },
			{    0, 120, 2, TILE,     BLOCK,   T1   }, {   80, 105, 2, TILE,     BLOCK,   T1   }, {   80, 115, 2, TILE,     BLOCK,   T1   }, {   20, 145, 2, TILE,     BLOCK,   T1   },
			{    0, 165, 2, TILE,     BLOCK,   T1   }, {   20, 185, 2, TILE,     BLOCK,   T1   }, {   20, 210, 2, TILE,     BLOCK,   T1   }, {   20, 220, 2, TILE,     BLOCK,   T1   },
			{   20, 230, 2, TILE,     BLOCK,   T1   }, {   20, 240, 2, TILE,     BLOCK,   T1   }, {   20, 250, 2, TILE,     BLOCK,   T1   }, {   50, 160, 2, TILE,     BLOCK,   T1   },
			{   50, 170, 2, TILE,     BLOCK,   T1   }, {  105, 135, 2, TILE,     BLOCK,   T1   }, {  105, 150, 2, TILE,     BLOCK,   T1   }, {  105, 160, 2, TILE,     BLOCK,   T1   },
			{  105, 170, 2, TILE,     BLOCK,   T1   }, {  150, 180, 2, TILE,     BLOCK,   T1   }, {  150, 190, 2, TILE,     BLOCK,   T1   }, {  150, 230, 2, TILE,     BLOCK,   T1   },
			{  150, 240, 2, TILE,     BLOCK,   T1   }, {  150, 250, 2, TILE,     BLOCK,   T1   }, {  190, 140, 2, TILE,     BLOCK,   T1   }, {  190, 150, 2, TILE,     BLOCK,   T1   },
			{  190, 160, 2, TILE,     BLOCK,   T1   }, {  190, 170, 2, TILE,     BLOCK,   T1   }, {    5,  30, 1, ITEM,     HEART,   BL1  }, {    5,  85, 1, ITEM,     CHEST,   IG1  },
			{   60, 145, 1, ITEM,     CHEST,   IG1  }, {  180, 125, 1, ITEM,     CHEST,   IG1  }, {  160, 215, 1, ITEM,     CHEST,   IG1  }, {  115, 170, 4, TILE,     DOOR,    TE1  },
			{   60, 210, 5, CREATURE, BOOGIE,  CU2  }, {   30, 185, 5, CREATURE, BOOGIE,  CU2  }, {  180, 125, 5, CREATURE, BOOGIE,  CU2  }, {   40,  45, 6, TILE,     BUSH,    T1   },
			{   25,  95, 6, TILE,     BUSH,    T1   }, {   50, 100, 6, TILE,     BUSH,    T1   }, {  160, 135, 6, TILE,     BUSH,    T1   }, {   75, 155, 6, TILE,     BUSH,    T1   },
			{   45, 205, 6, TILE,     BUSH,    T1   }, {   80, 220, 6, TILE,     BUSH,    T1   }, {  110, 235, 6, TILE,     BUSH,    T1   }, {  175, 225, 6, TILE,     BUSH,    T1   },
			{  100,  45, 6, TILE,     CLOUD,   TL1  }, {   80,  75, 6, TILE,     CLOUD,   TL1  }, {  115, 115, 6, TILE,     CLOUD,   TL1  }, {    0,  40, 6, TILE,     CLOUD,   T1   },
			{  125,  45, 6, TILE,     CLOUD,   T1   }, {  165,  60, 6, TILE,     CLOUD,   T1   }, {  150,  75, 6, TILE,     CLOUD,   T1   }, {  140, 115, 6, TILE,     CLOUD,   T1   },
			{  155, 105, 1, ITEM,     BOTTLE_O,BP1  },
		}
	};

	public static final int playerData[][] = {
		/* 0=action, 1=speed, 2=power, 3=jump, 4=chests, 5=life, 6=ammo, 7=stat */
		{ 0, 2, 6, 0, 0, 5, 0, MOVING      }, /* P1: Player - first level */
		{ 0, 2, 6, 0, 0, 0, 0, MOVING      }, /* P2: Player - facing right */
		{ 0, 2, 6, 0, 0, 0, 0, MOVING|FLIP }, /* P3: Player - facing left */
	};

	public static final int creatureData[][] = {
		/* 0=action, 1=speed, 2=effect, 3=value, 4=life, 5=stat */
		{ 0,     0, 0,     0, 2, AGGRO             }, /* C1:  Creature - standing, facing right */
		{ 0,     0, 0,     0, 2, AGGRO|FLIP        }, /* C2:  Creature - standing, facing left */
		{ LEFT,  1, 0,     0, 2, AGGRO|MOBILE|FLIP }, /* CL1: Creature - move left, facing left */
		{ RIGHT, 1, 0,     0, 2, AGGRO|MOBILE      }, /* CR1: Creature - move right, facing right */
		{ UP,    1, 0,     0, 2, AGGRO|MOBILE      }, /* CU1: Creature - move up, facing right */
		{ UP,    1, 0,     0, 2, AGGRO|MOBILE|FLIP }, /* CU2: Creature - move up, facing left */
		{ DOWN,  1, 0,     0, 2, AGGRO|MOBILE      }, /* CD1: Creature - move down, facing right */
		{ DOWN,  1, 0,     0, 2, AGGRO|MOBILE|FLIP }, /* CD2: Creature - move down, facing left */
	};

	public static final int tileData[][] = {
		/* 0=action, 1=speed, 2=effect, 3=value, 4=life, 5=stat */
		{ 0,     0, 0,     0, 0, SOLID             }, /* T1:  Tile - solid */
		{ LEFT,  1, 0,     0, 0, SOLID|MOBILE      }, /* TL1: Tile - solid, move left */
		{ RIGHT, 1, 0,     0, 0, SOLID|MOBILE      }, /* TR1: Tile - solid, move right */
		{ UP,    1, 0,     0, 0, SOLID|MOBILE      }, /* TU1: Tile - solid, move up */
		{ DOWN,  1, 0,     0, 0, SOLID|MOBILE      }, /* TD1: Tile - solid, move down */
		{ 0,     0, EXIT,  0, 0, BUFF              }, /* TE1: Tile - buff: level exit */
	};

	public static final int itemData[][] = {
		/* 0=action, 1=speed, 2=effect, 3=value, 4=life, 5=stat */
		{ 0,     0, GOLD,  1, 0, BUFF              }, /* IG1: Item - buff: gold */
		{ 0,     0, LIFE,  1, 0, BUFF              }, /* BL1: Item - buff: life */
		{ 0,     0, AMMO,  5, 0, BUFF              }, /* BA1: Item - buff: ammo */
		{ 0,     0, POWER, 1, 0, BUFF              }, /* BP1: Item - buff: power*/
		{ 0,     0, SPEED, 1, 0, BUFF              }, /* BS1: Item - buff: speed */
	};

	private LevelFactory() {}

	public static int getMaxLevels() { return levels.length; }

	public static Level createLevel(Main g,int l) {
Main.log("LevelFactory.createLevel(1)");
		if(l<0 || l>=levels.length) l = 0;
		Player player = g.getPlayer();
		int lvl[][] = levels[l];
		int data[] = lvl[0],item[];
		Level tl = new Level(g,data[0],data[1],data[2],data[3],l);
		GameObject to;
		for(int i=1; i<lvl.length; ++i) {
			item = lvl[i];
			to = createObject(g,tl,item[3],item[0],item[1],item[2],item[4],item[5]);
			tl.addObject(to);
		}
		player.setCollect(data[4]);
		System.gc();
Main.log("LevelFactory.createLevel(2)");
		return tl;
	}

	public static GameObject createObject(Main g,Level l,int c,int x,int y,int z,int t,int d) {
		GameObject to;
		if(l==null) l = g.getLevel();
		switch(c) {
			case TILE:
				to = new Tile(g,l,x,y,z,t);
				to.setData(tileData[d]);
				break;

			case ITEM:
				to = new Item(g,l,x,y,z,t);
				to.setData(itemData[d]);
				break;

			case CREATURE:
				to = new Creature(g,l,x,y,z,t);
				to.setData(creatureData[d]);
				break;

			case PLAYER:
				to = g.getPlayer();
				to.setType(l,t);
				to.transport(x,y,z,false);
				to.setData(playerData[d]);
				break;

			default:return null;
		}
		return to;
	}
}
