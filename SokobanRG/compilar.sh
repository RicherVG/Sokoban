#!/bin/bash

echo "=== Limpiando clases antiguas ==="
rm -rf build/classes/* 2>/dev/null
mkdir -p build/classes

echo "=== Generando lista de fuentes ==="
find src -name "*.java" > sources_list.txt

echo "=== Compilando con javac ==="
javac -cp "libs/gdx.jar:libs/gdx-backend-lwjgl3.jar:libs/gdx-natives.jar:libs/lwjgl.jar:libs/lwjgl-glfw.jar:libs/lwjgl-openal.jar:libs/lwjgl-opengl.jar:libs/lwjgl-stb.jar:libs/lwjgl-jemalloc.jar:libs/jlayer-1.0.1-gdx.jar:libs/jorbis-0.0.17.jar:libs/gdx-jnigen-loader.jar:libs/lwjgl-glfw-natives-macos.jar:libs/lwjgl-glfw-natives-macos-arm64.jar:libs/lwjgl-jemalloc-natives-macos.jar:libs/lwjgl-jemalloc-natives-macos-arm64.jar:libs/lwjgl-natives-macos.jar:libs/lwjgl-natives-macos-arm64.jar:libs/lwjgl-openal-natives-macos.jar:libs/lwjgl-openal-natives-macos-arm64.jar:libs/lwjgl-opengl-natives-macos.jar:libs/lwjgl-opengl-natives-macos-arm64.jar:libs/lwjgl-stb-natives-macos.jar:libs/lwjgl-stb-natives-macos-arm64.jar" -d build/classes -encoding UTF-8 -source 21 -target 21 @sources_list.txt 2>&1

if [ $? -eq 0 ]; then
    echo ""
    echo "=== COMPILACION EXITOSA ==="
else
    echo ""
    echo "=== ERROR EN COMPILACION ==="
fi

rm sources_list.txt
