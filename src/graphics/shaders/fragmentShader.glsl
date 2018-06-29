#version 400 core

//in vec3 color;
out vec4 out_Color;

in vec2 fragTexCoord;
in vec3 fragPos;
in vec3 fragNormal;

in vec3 color;

uniform mat4 transformationMatrix;
uniform sampler2D texSampler;
uniform vec3 lightColor;
uniform vec4 lightPosition;
uniform float useFakeLighting;
uniform float outSelected;


void main(void){

    vec4 fragPosition = transformationMatrix * vec4(fragPos, 1.0);

    vec3 surfaceToLight;
    float attenuation = 1.0;

    if (lightPosition.w == 0.0){
        // directional light
        surfaceToLight = normalize(lightPosition.xyz);
        attenuation = 1.0;
    }
    else{
        // point light
        surfaceToLight = normalize(lightPosition.xyz - fragPosition.xyz);
        // attenuation...
    }

    vec3 actualNormal = fragNormal;
    if(useFakeLighting > 0.5)
        actualNormal = vec3(0.0,1.0,0.0);

    vec3 surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz;
    vec3 normal = normalize(surfaceNormal);

    vec4 textureColor = texture(texSampler, fragTexCoord);
    float brightness = max(dot(normal, surfaceToLight), 0.0);
    vec3 diffuse = brightness * textureColor.rgb * lightColor;

    float lightAmbientCoefficient = 0.5f;
    vec3 ambient = lightAmbientCoefficient * textureColor.rgb * lightColor;

    //if(textureColor.a < 0.5) discard;

    if(fragTexCoord !=0){
        out_Color = vec4(ambient + diffuse , textureColor.a);
    }
    else
        out_Color = vec4(color, 1.0);

    if ( outSelected > 0 ) {
        out_Color = vec4(out_Color.x, out_Color.y, 1, 1);
    }



}