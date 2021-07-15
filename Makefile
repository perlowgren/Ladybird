
LIBGDX_DIR=.libgdx
NAME=ladybirds-gift
PACKAGE=com.spirangle.ladybird
MAIN_CLASS=Main
EXTENSIONS=freetype

all: desktop

.PHONY: all setup desktop android clean

setup:
	@java -jar gdx-setup.jar \
	  --dir $(LIBGDX_DIR) \
	  --name $(NAME) \
	  --package $(PACKAGE) \
	  --mainClass $(MAIN_CLASS) \
	  --excludeModules html \
	  --extensions $(EXTENSIONS)
	@./setup

desktop:
	@cd $(LIBGDX_DIR); \
	  ./gradlew desktop:run; \
	    cd ..

android:
	@cd $(LIBGDX_DIR); \
	  ./gradlew android:installDebug android:run; \
	    cd ..

clean:
	@rm -rf $(LIBGDX_DIR)