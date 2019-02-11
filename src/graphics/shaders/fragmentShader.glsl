#version 400 core

//in vec3 color;
out vec4 out_Color;

in vec2 fragTexCoord;
in vec3 mvVertexPos;
in vec3 mvVertexNormal;
in vec3 toCameraVector;

in vec3 color;


uniform mat4 modelMatrix;
uniform sampler2D texSampler;
uniform float outSelected;

struct PointLight{
    vec3 color;
    vec4 position;
    vec3 attenuation;
};

struct Material{
    /*vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;*/
    float reflectance;
    float shineDamper;
    float useFakeLighting;
};

uniform PointLight pointLight;
uniform Material material;

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal, vec4 textureColor){
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    vec4 specColor = vec4(0, 0, 0, 0);

    vec3 toLightVector;

   if(material.useFakeLighting > 0.5)
      normal = vec3(0.0,1.0,0.0);

   if (light.position.w == 0.0)
       // directional light
       toLightVector = normalize(light.position.xyz);
   else
       // point light
       toLightVector = normalize(light.position.xyz - position);


    // Diffuse light
    float diffuseFactor = max(dot(normal, toLightVector), 0.0);

    // Specular Light
    vec3 lightDirection = -toLightVector;
    vec3 reflectedLightDirection = normalize(reflect(lightDirection, normal));
    float specularFactor = max( dot(toCameraVector, reflectedLightDirection), 0.0);
    specularFactor = pow(specularFactor, material.shineDamper) * material.reflectance;

    // attenuation
    float distance = length(toLightVector);
    float attenuationFactor = light.attenuation.x + light.attenuation.y * distance + light.attenuation.z * distance * distance;

    // ambient light
    vec4 ambient = textureColor * vec4(0.3f, 0.3f, 0.3f, 1.0f);
    return (diffuseFactor + specularFactor) * textureColor * vec4(light.color, 1.0) / attenuationFactor + ambient;
}

void main(void){

    vec4 textureColor;

    if(fragTexCoord !=0){
        textureColor = texture(texSampler, fragTexCoord);
    }else {
        textureColor = vec4(color, 1.0);
    }

    vec4 light = calcPointLight(pointLight, mvVertexPos, mvVertexNormal, textureColor);

    if(textureColor.a < 0.5) discard;

    out_Color = light;

    // selected object
    /*if ( outSelected > 0 ) {
        out_Color = vec4(out_Color.x, out_Color.y, 1, 1);
    }*/
}