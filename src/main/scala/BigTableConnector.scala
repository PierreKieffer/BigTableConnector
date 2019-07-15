import org.apache.hadoop.hbase.client.{Get, Put}

object BigTableConnector {

  import org.apache.hadoop.hbase.util.Bytes
  import com.google.cloud.bigtable.hbase.BigtableConfiguration
  import org.apache.hadoop.hbase.HColumnDescriptor
  import org.apache.hadoop.hbase.HTableDescriptor
  import org.apache.hadoop.hbase.client.Scan

  import org.apache.hadoop.hbase.TableName


  import java.io.IOException


  private val TABLE_NAME = Bytes.toBytes("Hello-Bigtable")
  private val COLUMN_FAMILY_NAME = Bytes.toBytes("cf1")
  private val COLUMN_NAME = Bytes.toBytes("greeting")

  private val GREETINGS = Array("Hello World!", "Hello Cloud Bigtable!", "Hello HBase!")


  /** Connects to Cloud Bigtable, runs some basic operations and prints the results. */
  def doHelloWorld(projectId: String, instanceId: String): Unit = {

    try {
      val connection = BigtableConfiguration.connect(projectId, instanceId)

      try {
        val admin = connection.getAdmin

        // Create a table with a single column family
        val descriptor = new HTableDescriptor(TableName.valueOf(TABLE_NAME))
        descriptor.addFamily(new HColumnDescriptor(COLUMN_FAMILY_NAME))
        println("Create table " + descriptor.getNameAsString)

//        admin.createTable(descriptor)
        admin.createTable(descriptor)

        // Retrieve the table we just created so we can do some reads and writes
        val table = connection.getTable(TableName.valueOf(TABLE_NAME))

        // Write some rows to the table
        var i = 0
        while ( {
          i < GREETINGS.length
        }) {
          val rowKey = "greeting" + i

          // Put a single row into the table. We could also pass a list of Puts to write a batch.
          val put = new Put(Bytes.toBytes(rowKey))
          put.addColumn(COLUMN_FAMILY_NAME, COLUMN_NAME, Bytes.toBytes(GREETINGS(i)))
          table.put(put)

          {
            i += 1; i - 1
          }
        }

        // Get the first greeting by row key
        val rowKey = "greeting0"
        val getResult = table.get(new Get(Bytes.toBytes(rowKey)))
        val greeting = Bytes.toString(getResult.getValue(COLUMN_FAMILY_NAME, COLUMN_NAME))
        System.out.println("Get a single greeting by row key")
        System.out.printf("\t%s = %s\n", rowKey, greeting)

        // Now scan across all rows.
        val scan = new Scan
        print("Scan for all greetings:")
        val scanner = table.getScanner(scan)
        import scala.collection.JavaConversions._
        for (row <- scanner) {
          val valueBytes = row.getValue(COLUMN_FAMILY_NAME, COLUMN_NAME)
          System.out.println('\t' + Bytes.toString(valueBytes))
        }

//        // Clean up by disabling and then deleting the table
//        print("Delete the table")
//        admin.disableTable(table.getName)
//        admin.deleteTable(table.getName)
      } catch {
        case e: IOException =>
          System.err.println("Exception while running HelloWorld: " + e.getMessage)
          e.printStackTrace()
          System.exit(1)
      } finally if (connection != null) connection.close()
    } catch {
      case e: IOException =>
        System.err.println("Exception while running HelloWorld: " + e.getMessage)
        e.printStackTrace()
        System.exit(1)
    }
    System.exit(0)
  }

  def main(args: Array[String]): Unit = { // Consult system properties to get project/instance
    val projectId = requiredProperty("flash-zenith-245106 ")
    val instanceId = requiredProperty("bigtabletest")
    doHelloWorld(projectId, instanceId)
  }

  private def requiredProperty(prop: String) = {
    val value = System.getProperty(prop)
    if (value == null) throw new IllegalArgumentException("Missing required system property: " + prop)
    value
  }

}
