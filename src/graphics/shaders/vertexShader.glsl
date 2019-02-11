#version 400 core

in vec3 position;
in vec2 textures;
in vec3 normal;

out vec2 fragTexCoord;
out vec3 mvVertexNormal;
out vec3 mvVertexPos;
out vec3 toCameraVector;

out vec3 color;

/*uniform float numberOfRows;
uniform vec2 offset;*/

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

//out vec3 color;


void main(void){

    vec4 mvPos = modelMatrix * vec4(position, 1.0);

    //gl_ClipDistance[0] = -1;

    color = vec3(0,0,0);

    gl_Position = projectionMatrix * viewMatrix * mvPos;

    mvVertexNormal = normalize(modelMatrix * vec4(normal, 0.0)).xyz; // in model coordinates
    mvVertexPos = mvPos.xyz;                                         // in model coordinates
    toCameraVector = normalize((inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - mvVertexPos); // in view coordinates

    fragTexCoord = textures;
}