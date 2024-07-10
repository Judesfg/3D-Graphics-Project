/* I declare that this code is my own work */
/* Author: Jude Gibson
/* Email: jsfgibson1@sheffield.ac.uk */
import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.util.texture.awt.*;
import com.jogamp.opengl.util.texture.spi.JPEGImage;
  
public class Aliens_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
    
  public Aliens_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(4f,12f,18f));
  }
  
  // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
    gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
    initialise(gl);
    startTime = getSeconds();
  }
  
  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float)width/(float)height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
  }

  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  /* Clean up memory, if necessary */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    light.dispose(gl);
    lightpost.dispose(gl);
    floor.dispose(gl);
    alien1.dispose(gl);
    alien2.dispose(gl);
    skybox.dispose(gl);
  }
  
  
  // ***************************************************
  /* INTERACTION
   *
   *
   */
   
  private boolean animation = false;
  private double savedTime = 0;
   
  public void startAnimation() {
    animation = true;
    startTime = getSeconds()-savedTime;
  }
   
  public void stopAnimation() {
    animation = false;
    double elapsedTime = getSeconds()-startTime;
    savedTime = elapsedTime;
  }
  
  public void dimLight() {
    light.dimLight();
  }

  public void brightLight() {
    light.brightLight();
  }
  
  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  // textures
  private TextureLibrary textures;

  private Camera camera;
  private Mat4 perspective;
  private Model floor, lightpost;
  private Skybox skybox;
  private Light light;
  //private SGNode alienRoot;
  
  private Alien alien1, alien2;

  private void initialise(GL3 gl) {
    createRandomNumbers();

    textures = new TextureLibrary();
    textures.add(gl, "skybox_front", "textures/skybox_front.jpg");
    textures.add(gl, "skybox_back", "textures/skybox_back.jpg");
    textures.add(gl, "skybox_floor", "textures/skybox_floor.jpg");
    textures.add(gl, "skybox_sky", "textures/skybox_sky.jpg");
    textures.add(gl, "skybox_left", "textures/skybox_left.jpg");
    textures.add(gl, "skybox_right", "textures/skybox_right.jpg");
    textures.add(gl, "alien_skin_colour", "textures/alien_skin_colour_new.jpg");
    textures.add(gl, "alien_skin_specular", "textures/alien_skin_specular.jpg");
    textures.add(gl, "alien_muscle_diffuse", "textures/Alien_Muscle_001_COLOR.jpg");
    textures.add(gl, "alien_muscle_specular", "textures/Alien_Muscle_001_SPEC.jpg");
    textures.add(gl, "eye_diffuse", "textures/eye_diffuse.jpg");
    textures.add(gl, "eye_specular", "textures/eye_specular.jpg");
    textures.add(gl, "eye2_diffuse", "textures/eye_invert_diffuse.jpg");
    textures.add(gl, "eye2_specular", "textures/eye_invert_specular.jpg");
    
    
    light = new Light(gl);
    light.setCamera(camera);

    Vec3 alien1pos = new Vec3(-3.5f,0f,0f);
    Vec3 alien2pos = new Vec3(3.5f,0f,0f);
    
    // floor
    String name = "floor";
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "vs_standard.txt", "fs_standard_1t.txt");
    Material material = new Material(new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    Mat4 modelMatrix = Mat4Transform.scale(16,1f,16);
    floor = new Model(name, mesh, modelMatrix, shader, material, light, camera, textures.get("skybox_floor"));

    // lightpost - New code
    name = "lightpost";
    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "vs_standard.txt", "fs_standard_1t.txt");
    material = new Material(new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(0.5f,5f,0.5f), Mat4Transform.translate(-10f, 0.5f, 8f));
    lightpost = new Model(name, mesh, modelMatrix, shader, material, light, camera, textures.get("skybox_floor"));
    
    /*The following models are all new code */
    alien1 = new Alien(gl, camera, light, alien1pos,
                      textures.get("alien_skin_colour"), textures.get("alien_skin_specular"),
                      textures.get("eye_diffuse"), textures.get("eye_specular"),
                      textures.get("watt_diffuse"), textures.get("watt_specular")); 

    alien2 = new Alien(gl, camera, light, alien2pos,
                      textures.get("alien_muscle_diffuse"), textures.get("alien_muscle_specular"),
                      textures.get("eye2_diffuse"), textures.get("eye2_specular"),
                      textures.get("watt_diffuse"), textures.get("watt_specular")); 

    skybox = new Skybox(gl, camera, light, 
                      textures.get("skybox_front"), 
                      textures.get("skybox_sky"), 
                      textures.get("skybox_left"), 
                      textures.get("skybox_back"), 
                      textures.get("skybox_floor"), 
                      textures.get("skybox_right"));
  }
 
  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    //light.setDirection(getLightDirection());  // changing light position each frame
    light.render(gl);
    lightpost.render(gl);
    floor.render(gl); 
    skybox.updatePosition(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
    skybox.render(gl);
    if (animation) {
      double elapsedTime = getSeconds()-startTime;
      alien1.updateAnimation(elapsedTime);
      alien2.updateAnimation(elapsedTime);
    }
    alien1.render(gl);
    alien2.render(gl);
  }

  
  
  // The light's postion is continually being changed, so needs to be calculated for each frame.
  private Vec3 getLightDirection() {
    double elapsedTime = getSeconds()-startTime;
    float x = light.getStartDirection().x;//*(float)(Math.sin(Math.toRadians(elapsedTime*50)));
    float y = light.getStartDirection().y;
    float z = light.getStartDirection().z*(float)(Math.cos(Math.toRadians(elapsedTime*50)));
    return new Vec3(x,y,z);   
    //return new Vec3(5f,3.4f,5f);
  }

  
  // ***************************************************
  /* TIME
   */ 
  
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

  // ***************************************************
  /* An array of random numbers
   */ 
  
  private int NUM_RANDOMS = 1000;
  private float[] randoms;
  
  private void createRandomNumbers() {
    randoms = new float[NUM_RANDOMS];
    for (int i=0; i<NUM_RANDOMS; ++i) {
      randoms[i] = (float)Math.random();
    }
  }
  
}