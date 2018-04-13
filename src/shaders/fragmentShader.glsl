#version 400 core

//in vec3 color;
in vec2 pass_texCoord;
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 out_Color;

uniform sampler2D texSampler;
uniform vec3 lightColor;

void main(void){

   vec3 unitNormal = normalize(surfaceNormal);
   vec3 unitLightVector = normalize(toLightVector);

   float nDot1 = dot(unitNormal, unitLightVector);
   float brightness = max(nDot1, 0.0);
   vec3 diffuse = brightness * lightColor;

    vec4 textureColor = texture(texSampler, pass_texCoord);
    //if(textureColor.a < 0.5) discard;
    out_Color = vec4(diffuse, 1.0) * textureColor;
}