/* I declare that this code is my own work */
/* Author: Jude Gibson
/* Email: jsfgibson1@sheffield.ac.uk */

/*
 * This class is completely new and defines the skybox that surrounds the camera.
 */
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

public class Skybox {
    
  private Camera camera;
  private Light light;

  private Model posx, posy, posz, negx, negy, negz;

  private SGNode skyboxRoot;
  private TransformNode skyboxMoveTranslate;

  public Skybox(GL3 gl, Camera cameraIn, Light lightIn, Texture t1, Texture t2, Texture t3, Texture t4, Texture t5, Texture t6) {

    this.camera = cameraIn;
    this.light = lightIn;

    posx = makeSkyboxFace(gl, t1);
    posy = makeSkyboxFace(gl, t2);
    posz = makeSkyboxFace(gl, t3);
    negx = makeSkyboxFace(gl, t4);
    negy = makeSkyboxFace(gl, t5);
    negz = makeSkyboxFace(gl, t6);

    float skyboxWidth = 110f;
    Mat4 m;

    skyboxRoot = new NameNode("root");
    skyboxMoveTranslate = new TransformNode("skybox transform",Mat4Transform.translate(this.camera.getPosition().x,this.camera.getPosition().y,this.camera.getPosition().z));

    TransformNode skyboxTranslate = new TransformNode("skybox transform",Mat4Transform.translate(0,0,0));

    NameNode floor = new NameNode("floor");
      TransformNode floorScale = new TransformNode("floor scale", Mat4Transform.scale(skyboxWidth,0,skyboxWidth));
        TransformNode floorTranslate = new TransformNode("floor translate", Mat4Transform.translate(0,-(skyboxWidth/2),0));
          TransformNode floorRotate = new TransformNode("floor rotate", Mat4Transform.rotateAroundY(90));
        ModelNode floorShape = new ModelNode("floor", negy);

    NameNode sky = new NameNode("sky");
      TransformNode skyScale = new TransformNode("sky scale", Mat4Transform.scale(skyboxWidth,0,skyboxWidth));
        TransformNode skyTranslate = new TransformNode("sky translate", Mat4Transform.translate(0,skyboxWidth/2,0));
          m = Mat4Transform.rotateAroundX(180);
          TransformNode skyRotate = new TransformNode("sky rotate", Mat4.multiply(m, Mat4Transform.rotateAroundY(270)));
            ModelNode skyShape = new ModelNode("sky", posy);

    NameNode front = new NameNode("front");
      TransformNode frontScale = new TransformNode("front scale", Mat4Transform.scale(skyboxWidth,0,skyboxWidth));
        TransformNode frontTranslate = new TransformNode("front translate", Mat4Transform.translate(0,0,-(skyboxWidth/2)));
          TransformNode frontRotate = new TransformNode("front rotate", Mat4Transform.rotateAroundX(90));
        ModelNode frontShape = new ModelNode("front", posx);

    NameNode back = new NameNode("back");
      TransformNode backScale = new TransformNode("back scale", Mat4Transform.scale(skyboxWidth,0,skyboxWidth));
        TransformNode backTranslate = new TransformNode("back translate", Mat4Transform.translate(0,0,skyboxWidth/2));
          m = Mat4Transform.rotateAroundX(270);
          TransformNode backRotate = new TransformNode("back rotate", Mat4.multiply(m, Mat4Transform.rotateAroundY(180)));
        ModelNode backShape = new ModelNode("back", negx);

    NameNode left = new NameNode("left");
      TransformNode leftScale = new TransformNode("left scale", Mat4Transform.scale(skyboxWidth,0,skyboxWidth));
        TransformNode leftTranslate = new TransformNode("left translate", Mat4Transform.translate(-(skyboxWidth/2),0,0));
          m = Mat4Transform.rotateAroundX(90);
          TransformNode leftRotate = new TransformNode("back rotate", Mat4.multiply(m, Mat4Transform.rotateAroundZ(-90)));
        ModelNode leftShape = new ModelNode("left", posz);

    NameNode right = new NameNode("right");
      TransformNode rightScale = new TransformNode("right scale", Mat4Transform.scale(skyboxWidth,0,skyboxWidth));
        TransformNode rightTranslate = new TransformNode("right translate", Mat4Transform.translate(skyboxWidth/2,0,0));
          m = Mat4Transform.rotateAroundX(90);
          TransformNode rightRotate = new TransformNode("right rotate", Mat4.multiply(m, Mat4Transform.rotateAroundZ(90)));
        ModelNode rightShape = new ModelNode("right", negz);


    skyboxRoot.addChild(skyboxMoveTranslate);
      skyboxMoveTranslate.addChild(floor);
        floor.addChild(floorTranslate);
          floorTranslate.addChild(floorRotate);
          floorRotate.addChild(floorScale);
          floorScale.addChild(floorShape);
        floor.addChild(sky);
          sky.addChild(skyTranslate);
          skyTranslate.addChild(skyRotate);
          skyRotate.addChild(skyScale);
          skyScale.addChild(skyShape);
        floor.addChild(front);
          front.addChild(frontTranslate);
          frontTranslate.addChild(frontRotate);
          frontRotate.addChild(frontScale);
          frontScale.addChild(frontShape);
        floor.addChild(back);
          back.addChild(backTranslate);
          backTranslate.addChild(backRotate);
          backRotate.addChild(backScale);
          backScale.addChild(backShape);
        floor.addChild(right);
          right.addChild(rightTranslate);
          rightTranslate.addChild(rightRotate);
          rightRotate.addChild(rightScale);
          rightScale.addChild(rightShape);
        floor.addChild(left);
          left.addChild(leftTranslate);
          leftTranslate.addChild(leftRotate);
          leftRotate.addChild(leftScale);
          leftScale.addChild(leftShape);

    skyboxRoot.update();

  }

  public void updatePosition(float x, float y, float z){
    skyboxMoveTranslate.setTransform(Mat4Transform.translate(x,y,z));
    skyboxMoveTranslate.update();
  }

  public void render(GL3 gl) {
    skyboxRoot.draw(gl);
  }

  private Model makeSkyboxFace(GL3 gl, Texture t){
    String name = "face";
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "vs_standard.txt", "fs_standard_1t.txt");
    Material material = new Material(new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
    Mat4 modelMatrix = Mat4Transform.scale(64,1f,64);
    return new Model(name, mesh, modelMatrix, shader, material, light, camera, t);
  }

  public void dispose(GL3 gl) {
    posx.dispose(gl);
    posy.dispose(gl);
    posz.dispose(gl);
    negx.dispose(gl);
    negy.dispose(gl);
    negz.dispose(gl);
  }
}
