@echo off
echo === Limpiando clases antiguas ===
del /s /q build\classes\*.class 2>nul

echo === Generando lista de fuentes ===
dir /s /b src\*.java > sources_list.txt

echo === Compilando con javac ===
javac -cp "libs\gdx.jar;libs\gdx-backend-lwjgl3.jar;libs\gdx-natives.jar;libs\lwjgl.jar;libs\lwjgl-glfw.jar;libs\lwjgl-openal.jar;libs\lwjgl-opengl.jar;libs\lwjgl-stb.jar;libs\lwjgl-jemalloc.jar;libs\jlayer-1.0.1-gdx.jar;libs\jorbis-0.0.17.jar;libs\gdx-jnigen-loader.jar;libs\lwjgl-glfw-natives-windows.jar;libs\lwjgl-jemalloc-natives-windows.jar;libs\lwjgl-natives-windows.jar;libs\lwjgl-openal-natives-windows.jar;libs\lwjgl-opengl-natives-windows.jar;libs\lwjgl-stb-natives-windows.jar" -d build\classes -encoding UTF-8 -source 21 -target 21 @sources_list.txt 2>&1
if %ERRORLEVEL% == 0 (
    echo.
    echo === COMPILACION EXITOSA ===
) else (
    echo.
    echo === ERROR EN COMPILACION ===
)
del sources_list.txt
