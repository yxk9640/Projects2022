import org.apache.spark._
import org.apache.spark.rdd._
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

import java.io.PrintWriter

object Add {
  val rows = 100
  val columns = 100

  case class Block ( data: Array[Double] ) {

    override
    def toString (): String = {
      var s = "\n"
      for ( i <- 0 until rows ) {
        for ( j <- 0 until columns )
          s += "\t%.3f".format(data(i*rows+j))
        s += "\n"
      }
      s
    }
  }

  /* Convert a list of triples (i,j,v) into a Block */
  def toBlock(triples: List[(Int, Int, Double)]): Block = {
    /* ... */
//    data(triples(0)._1 * rows + triples(0)._2) = triples(0)._3
    val data: Array[Double] = new Array[Double](rows*columns)
    for (i <- 0 until rows) {
      for (j <- 0 until columns) {
        data(i*rows+j) = 0.00
//        data(triples(0)._1%rows * rows + triples(0)._2%columns) = triples(i)._3
      }
    }
    for (i <- 0 until triples.length) {
      for (j <- 0 until triples.length) {
                data(triples(i)._1%rows * rows + triples(i)._2%columns) = triples(i)._3
      }
    }

    val bl = Block.apply(data)
    bl
  }

  /* Add two Blocks */
  def blockAdd ( m: Block, n: Block ): Block = {
    /* ... */
        for (i <- 0 until rows) {
          for (j <- 0 until columns){
            if (m.data(i*rows+j).equals(n.data(i*rows+j))) {
              m.data(i*rows+j) += n.data(i*rows+j)
            }
          }
        }

    return m
  }

  /* Read a sparse matrix from a file and convert it to a block matrix */
  def createBlockMatrix ( sc: SparkContext, file: String ): RDD[((Int,Int),Block)] = {
//  def createBlockMatrix ( sc: SparkContext, file: String ): Unit = {
    /* ... */
    val Sparse = sc.textFile(file).map( line =>{
              val m = line.split(",")
              (m(0).toInt, m(1).toInt, m(2).toDouble)
          })
    val SparseList = Sparse.collect().toList
    val Block = Sparse.map( MSparse => ( ( MSparse._1/rows,MSparse._2/columns), toBlock(SparseList) ))
    //place block in corresponding co-ordinates //    Map((Int,Int)->Double)
    Block
  }


  def main ( args: Array[String] ) {
    /* ... */
    val conf = new SparkConf().setAppName("Add")
//    conf.setMaster("local[2]")
    val sc = new SparkContext(conf)

    //input matrix m
    val createBlockMatrixM = createBlockMatrix(sc,args(0)) //return block matrix - M
    val createBlockMatrixN = createBlockMatrix(sc,args(1)) //return block matrix - N

//    val res = e.map(e => (e._2, e)).join(d.map(d => (d._2, d)))
//      .map { case (k, (e, d)) => e._1 + " " + d._1 }

    val res =

//    val result = blockAdd(createBlockM,createBlockN)

//    result.saveAsTextFile(args(2))
    createBlockMatrixM.saveAsTextFile(args(2))
    sc.stop()

  }
}