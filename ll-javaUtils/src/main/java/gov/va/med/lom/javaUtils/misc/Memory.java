package gov.va.med.lom.javaUtils.misc;

public class Memory {

  private Runtime runtime;

  public Memory(Runtime runtime) {
    this.runtime = runtime;
  }

  public long getFreeMemory() {
    /* Free memory is the number of bytes available to the VM to create objects 
       from the section of memory it controls (i.e. memory already allocated to
       the runtime by the underlying system).  The free memory increases when a
       garbace collection successfully reclaims space used by dead objects, and
       also increases when the Java runtime requests more memory from the underlying
       operating system.  The free memory reduces each time an object is created, 
       and also when the runtime returns memory to the underlying system.
    */
    return runtime.freeMemory();
  }

  public long getTotalMemory() {
    /* Total memory is the number of bytes currently allocated to the runtime
       system for this particular Java VM process.  Within this memory allocation, 
       the VM manages its objects and data.  Some of this allocated memory is held
       in reserve for creating new objects.  When the currently allocated memory 
       gets filled and the garbage collector cannot allocate sufficiently more memory,
       the VM requests more memory to be allocated to it from the underlying system. 
       If the underlying system cannot allocate any further memory, an OutOfMemoryError 
       is thrown.
    */
    return runtime.totalMemory();
  }

  public long getMaxMemory() {
    return runtime.maxMemory();
  }

  public void printMemory() {
    System.out.println("Free Memory = " + getFreeMemory());
    System.out.println("Total Memory = " + getTotalMemory());
    System.out.println("Max Total Memory = " + getMaxMemory());
  }

  public void garbageCollect() {
    runtime.gc();
  }

  public static void main(String[] args) {
    Memory memory = new Memory(Runtime.getRuntime());
    memory.printMemory();
  }
}