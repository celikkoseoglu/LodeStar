precision mediump float; 
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate; 
varying vec4 m_alpha;
vec4 d;

void main() 
{
       vec2 st = v_TexCoordinate.st;
       d = vec4(0.9,0.9,0.9,0.9);

       if( m_alpha.r > 0.0 && m_alpha.g > 0.0 && m_alpha.b > 0.0 && m_alpha.a > 0.0 ){
            gl_FragColor = (texture2D(u_Texture, st))*d;
       }
       else{
           	st.s = 1. - st.s;
           	//st.t = 1. - st.t;

            gl_FragColor = (texture2D(u_Texture, st));
       }
}