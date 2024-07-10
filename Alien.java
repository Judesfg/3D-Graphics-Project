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

public class Alien {

  private Camera camera;
  private Light light;

  private Model sphere, cube, cube2, eye;

  private SGNode alienRoot;
  private float xPosition = 0;
  private float leftArmAngle = 135f;
  private float rightArmAngle = 225f;
  private TransformNode translateX, alienMoveTranslate, leftArmRotate, rightArmRotate, headRotate;
  private NameNode rightarm;
  private Vec3 position;
   
  public Alien(GL3 gl, Camera cameraIn, Light lightIn, Vec3 pos, Texture t1, Texture t2, Texture t3, Texture t4, Texture t5, Texture t6) {

    this.camera = cameraIn;
    this.light = lightIn;

    sphere = makeSphere(gl, t1,t2);
    eye = makeSphere(gl, t3, t4);

    position = pos;

    // alien
    
    float bodyHeight = 3f;
    float bodyWidth = 3f;
    float bodyDepth = 3f;
    float headScale = 2f;
    float armLength = 3f;
    float armScale = 0.4f;
    float legLength = 3.5f;
    float legScale = 0.67f;
    float earScale = 0.25f;
    float earLength = 1.2f;
    float antennaLowerLength = 0.5f;
    float antennaLowerScale = 0.1f;
    float antennaUpperScale = 0.2f;
    float eyeWidth = 0.25f;
    float eyeLength = 0.4f;
    float eyeHeight = 0.25f;
  

    
    alienRoot = new NameNode("root");
    alienMoveTranslate = new TransformNode("alien transform",Mat4Transform.translate(pos.x,pos.y,pos.z));
    
    TransformNode alienTranslate = new TransformNode("alien transform",Mat4Transform.translate(0,0,0));
    
    NameNode body = new NameNode("body");
      Mat4 m = Mat4Transform.scale(bodyWidth,bodyHeight,bodyDepth);
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode bodyTransform = new TransformNode("body transform", m);
        TransformNode bodyRotate = new TransformNode("body rotate", Mat4Transform.rotateAroundZ(0));
        ModelNode bodyShape = new ModelNode("Sphere(body)", sphere);

    NameNode head = new NameNode("head"); 
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate(0,bodyHeight,0));
      m = Mat4.multiply(m, Mat4Transform.scale(headScale,headScale,headScale));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode headTransform = new TransformNode("head transform", m);
        TransformNode headRotate = new TransformNode("head rotate", Mat4Transform.rotateAroundX(0));
          ModelNode headShape = new ModelNode("Sphere(head)", sphere);

   // This set of nodes includes nodes for creating the arm and controlling its animation and attaching it to the body.
   NameNode leftarm = new NameNode("left arm");
      TransformNode leftArmTranslate = new TransformNode("leftarm translate", 
                                           Mat4Transform.translate((bodyWidth*0.85f)-armLength/2,bodyHeight*0.7f,0));
      leftArmRotate = new TransformNode("leftarm rotate",Mat4Transform.rotateAroundZ(leftArmAngle));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(armScale,armLength,armScale));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0f,0));
      TransformNode leftArmScale = new TransformNode("leftarm scale", m);
        ModelNode leftArmShape = new ModelNode("Cube(left arm)", sphere);

    // similar to the left arm
    rightarm = new NameNode("right arm");
      TransformNode rightArmTranslate = new TransformNode("rightarm translate", 
                                            Mat4Transform.translate((bodyWidth*-0.85f)+armLength/2,bodyHeight*0.7f,0));
      rightArmRotate = new TransformNode("rightarm rotate",Mat4Transform.rotateAroundZ(rightArmAngle));
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(armScale,armLength,armScale));
      m = Mat4.multiply(m, Mat4Transform.translate(0f,0f,0));
      TransformNode rightArmScale = new TransformNode("rightarm scale", m);
        ModelNode rightArmShape = new ModelNode("Cube(right arm)", sphere);

    NameNode leftEar = new NameNode("left ear");
      TransformNode leftEarTranslate = new TransformNode("leftear translate", Mat4Transform.translate((headScale/2), bodyHeight+(headScale/2)+(earLength/2), 0));
        TransformNode leftEarScale = new TransformNode("leftear scale", Mat4Transform.scale(earScale, earLength, earScale));
        ModelNode leftEarShape = new ModelNode("Sphere(left ear)", sphere);

    NameNode rightEar = new NameNode("right ear");
      TransformNode rightEarTranslate = new TransformNode("rightear translate", Mat4Transform.translate(-(headScale/2), bodyHeight+(headScale/2)+(earLength/2), 0));
        TransformNode rightEarScale = new TransformNode("rightear scale", Mat4Transform.scale(earScale, earLength, earScale));
        ModelNode rightEarShape = new ModelNode("Sphere(right ear)", sphere);

    NameNode antennaLower = new NameNode("antenna lower");
      TransformNode antennaLowerTranslate = new TransformNode("antenna lower translate", Mat4Transform.translate(0, bodyHeight+headScale+(antennaLowerLength/2), 0));
        TransformNode antennaLowerTransformScale = new TransformNode("antenna lower scale", Mat4Transform.scale(antennaLowerScale, antennaLowerLength, antennaLowerScale));
        ModelNode antennaLowerShape = new ModelNode("Sphere(antenna lower)", sphere);

    NameNode antennaUpper = new NameNode("antenna upper");
      TransformNode antennaUpperTranslate = new TransformNode("antenna upper translate", Mat4Transform.translate(0, bodyHeight+headScale+(antennaLowerLength), 0));
        TransformNode antennaUpperTransformScale = new TransformNode("antenna upper scale", Mat4Transform.scale(antennaUpperScale, antennaUpperScale, antennaUpperScale));
        ModelNode antennaUpperShape = new ModelNode("Sphere(antenna upper)", sphere);

    NameNode leftEye = new NameNode("left eye");
      TransformNode leftEyeTranslate = new TransformNode("left eye translate", Mat4Transform.translate(headScale*0.25f, bodyHeight+(headScale*0.6f), headScale*0.5f-(eyeWidth/4)));
        TransformNode leftEyeScale = new TransformNode("left eye scale", Mat4Transform.scale(eyeLength, eyeHeight, eyeWidth));
          TransformNode leftEyeRotate = new TransformNode("left eye rotate", Mat4Transform.rotateAroundY(180));
            ModelNode leftEyeShape = new ModelNode("Sphere(left eye)", eye);

    NameNode rightEye = new NameNode("right eye");
      TransformNode rightEyeTranslate = new TransformNode("right eye translate", Mat4Transform.translate(-1*headScale*0.25f, bodyHeight+(headScale*0.6f), headScale*0.5f-(eyeWidth/4)));
        TransformNode rightEyeScale = new TransformNode("right eye scale", Mat4Transform.scale(eyeLength, eyeHeight, eyeWidth));
          TransformNode rightEyeRotate = new TransformNode("right eye rotate", Mat4Transform.rotateAroundY(180));
            ModelNode rightEyeShape = new ModelNode("Sphere(right eye)", eye);
    
    alienRoot.addChild(alienMoveTranslate);
      alienMoveTranslate.addChild(alienTranslate);
        alienTranslate.addChild(body);
          body.addChild(bodyTransform);
            bodyTransform.addChild(bodyShape);
          body.addChild(headRotate);
            headRotate.addChild(head);
              head.addChild(headTransform);
                headTransform.addChild(headShape);
                head.addChild(leftEar);
                  leftEar.addChild(leftEarTranslate);
                  leftEarTranslate.addChild(leftEarScale);
                  leftEarScale.addChild(leftEarShape);
                head.addChild(rightEar);
                  rightEar.addChild(rightEarTranslate);
                  rightEarTranslate.addChild(rightEarScale);
                  rightEarScale.addChild(rightEarShape);
                head.addChild(antennaLower);
                  antennaLower.addChild(antennaLowerTranslate);
                  antennaLowerTranslate.addChild(antennaLowerTransformScale);
                  antennaLowerTransformScale.addChild(antennaLowerShape);
                  antennaLower.addChild(antennaUpper);
                    antennaUpper.addChild(antennaUpperTranslate);
                    antennaUpperTranslate.addChild(antennaUpperTransformScale);
                    antennaUpperTransformScale.addChild(antennaUpperShape);
                head.addChild(leftEye);
                  leftEye.addChild(leftEyeTranslate);
                  leftEyeTranslate.addChild(leftEyeRotate);
                  leftEyeRotate.addChild(leftEyeScale);
                  leftEyeScale.addChild(leftEyeShape);
                head.addChild(rightEye);
                  rightEye.addChild(rightEyeTranslate);
                  rightEyeTranslate.addChild(rightEyeRotate);
                  rightEyeRotate.addChild(rightEyeScale);
                  rightEyeScale.addChild(rightEyeShape);
          body.addChild(leftarm);
            leftarm.addChild(leftArmTranslate);
            leftArmTranslate.addChild(leftArmRotate);
            leftArmRotate.addChild(leftArmScale);
            leftArmScale.addChild(leftArmShape);
          body.addChild(rightarm);
            rightarm.addChild(rightArmTranslate);
            rightArmTranslate.addChild(rightArmRotate);
            rightArmRotate.addChild(rightArmScale);
            rightArmScale.addChild(rightArmShape);
    
    alienRoot.update();  
  }

  /*Left over code from the tutorials. */
  private Model makeSphere(GL3 gl, Texture tex1, Texture tex2) {
    String name= "sphere";
    Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    Shader shader = new Shader(gl, "vs_standard.txt", "fs_standard_2t.txt");
    Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    Model sphere = new Model(name, mesh, modelMatrix, shader, material, light, camera, tex1, tex2);
    return sphere;
  } 

  /*Left over code from the tutorials. */
  private Model makeCube(GL3 gl, Texture tex1, Texture tex2) {
    String name= "cube";
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "vs_standard.txt", "fs_standard_2t.txt");
    Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    Model cube = new Model(name, mesh, modelMatrix, shader, material, light, camera, tex1, tex2);
    return cube;
  } 
/*Left over code from the tutorials. */
  public void render(GL3 gl) {
    alienRoot.draw(gl);
  }


  /*New code */
  public void updateAnimation(double elapsedTime) {
    float rightRotateAngle = rightArmAngle+45f*(float)Math.sin(elapsedTime*4);
    float bodyRotateAngle = 5f*(float)Math.sin(elapsedTime*4);
    alienMoveTranslate.setTransform(Mat4.multiply(Mat4Transform.translate(position.x,position.y,position.z) ,Mat4Transform.rotateAroundZ(bodyRotateAngle)));
    alienMoveTranslate.update();
    rightArmRotate.setTransform(Mat4Transform.rotateAroundZ(rightRotateAngle));
    rightArmRotate.update();
  }


  public void rollHead(double elapsedTime) {
    float headRollAngle = 5f*(float)Math.sin(elapsedTime*4);
    float headRollAmount = 0.1f*(float)Math.sin(elapsedTime*4);
    Mat4 m = Mat4.multiply(Mat4Transform.rotateAroundX(headRollAngle), Mat4Transform.translate(0,0, headRollAmount));
    headRotate.setTransform(m);
    headRotate.update();
  }

  /*Left over code from the tutorials. */
  public void dispose(GL3 gl) {
    sphere.dispose(gl);
  }
}