#version 400 core

in vec3 position;
in vec2 textures;
in vec3 normal;

out vec2 pass_texCoord;
out vec3 surfaceNormal;
out vec3 toLightVector;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;
//out vec3 color;


void main(void){
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    pass_texCoord = textures;

    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
    toLightVector = lightPosition - worldPosition.xyz;

    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
    //color = vec3(position.x + 0.5, position.y + 0.5, position.z + 0.5);
}