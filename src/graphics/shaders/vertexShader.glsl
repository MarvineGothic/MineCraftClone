#version 400 core

in vec3 position;
in vec2 textures;
in vec3 normal;

out vec2 fragTexCoord;
out vec3 fragPos;
out vec3 fragNormal;

out vec3 color;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

//out vec3 color;


void main(void){

    fragTexCoord = textures;
    fragPos = position;
    fragNormal = normal;

    color = vec3(position.x + 0.5, 1.0, position.y + 0.5);

    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
    //color = vec3(position.x + 0.5, position.y + 0.5, position.z + 0.5);
}