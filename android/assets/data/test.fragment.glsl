#ifdef GL_ES 
precision mediump float;
#endif

varying vec2 v_texCoord0;


// REd green blue
// In the main method we set the red component of the color of the fragment to the texture X coordinate (U) and the green component to the texture Y coordinate (V).
void main() {
    gl_FragColor = vec4(v_texCoord0, 0.0, 1.0);
}
