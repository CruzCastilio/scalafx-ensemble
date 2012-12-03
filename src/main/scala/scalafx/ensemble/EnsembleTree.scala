package scalafx.ensemble

import java.io.File

import scalafx.scene.control.Label
import scalafx.scene.control.TreeItem
import scalafx.scene.control.TreeItem.sfxTreeItemTojfx
import scalafx.scene.image.Image
import scalafx.scene.image.Image.sfxImage2jfx
import scalafx.scene.image.ImageView

/**
 * Object to load examples as Map which in turn is used
 * to create TreeItem in the UI
 */
object EnsembleTree {

  val fil = new File(getClass().getResource("/ensemble/examples").getPath())

  private def createTree() = {
    var egPlesTree = Map[String, List[TreeItem[String]]]()
    fil.listFiles().foreach(x => {
      if (x.isDirectory()) {
        var leaves = List[TreeItem[String]]()
        x.listFiles().foreach(a => {
          val leafname = a.getName().split(".txt")
          leaves = leaves.::(new TreeItem(leafname(0)))
        })
        //Sort the Children in the TreeView
        leaves = leaves sortWith {
          //compareLeaves
          _.getValue.toLowerCase < _.getValue.toLowerCase
        }
        //TreeNodes are capitalized
        egPlesTree = egPlesTree.+((x.getName().capitalize, leaves))
      }
    })
    egPlesTree
  }

  private def createThumbnails() = {
    var thumbnails = Map[String, List[EnsembleThumbNail]]()
    fil.listFiles().foreach(x => {
      if (x.isDirectory()) {
        var thumbs = List[EnsembleThumbNail]()
        x.listFiles().foreach(a => {
          val leafname = a.getName().split(".txt")
          val img = new ImageView()
          img.image = new Image(this.getClass.getResourceAsStream("/scalafx/ensemble/images/CalendarTextFieldSample.png"))
          val lbl = new Label()
          lbl.text = leafname(0)
          thumbs = thumbs.::(EnsembleThumbNail(img, lbl))
        })
        thumbnails = thumbnails.+((x.getName(), thumbs))
      }
    })
    thumbnails
  }

  def create() = {
    new EnsembleTree(createTree, createThumbnails)
  }

  //def compareLeaves(e1: TreeItem[String], e2: TreeItem[String]) = (e1.getValue().toLowerCase() < e2.getValue().toLowerCase())

}

case class EnsembleThumbNail(imgView: ImageView, caption: Label)

/**
 * The class provide accessibility methods to access the
 * underlying map
 */
class EnsembleTree(map: Map[String, List[TreeItem[String]]],
  thumbnails: Map[String, List[EnsembleThumbNail]]) {

  def getLeaves(keyName: String) = {
    map.get(keyName).get
  }

  /**
   * returns the entire tree
   */
  def getTree() = {
    var treeSibls = List[TreeItem[String]]()
    map.foreach(x => {
      val sibl = new TreeItem[String](x._1)
      sibl.expanded = true
      x._2.foreach(y => {
        println(y)
        sibl.getChildren().add(y)
      })
      treeSibls = treeSibls.::(sibl)
    })
    treeSibls
  }
}

