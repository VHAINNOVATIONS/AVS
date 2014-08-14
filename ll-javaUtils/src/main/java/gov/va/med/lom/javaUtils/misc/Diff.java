package gov.va.med.lom.javaUtils.misc;

import java.util.Hashtable;

public class Diff {

  // 1 more than the maximum equivalence value used for this or its sibling file. 
  private int equivMax = 1;
  
  // When set to true, the comparison uses a heuristic to speed it up.
  // With this heuristic, for files with a constant small density
  // of changes, the algorithm is linear in the file size. 
  public boolean heuristic = false;
  
  // When set to true, the algorithm returns a guarranteed minimal
  // set of changes.  This makes things slower, sometimes much slower.
  public boolean noDiscards = false;
  
  // Vectors being compared.
  private int[] xvec, yvec;
  
  // Vector, indexed by diagonal, containing the X coordinate of the point furthest
  // along the given diagonal in the forward search of the edit matrix.
  private int[] fdiag;
  
  // Vector, indexed by diagonal, containing the X coordinate of the point furthest
  // along the given diagonal in the backward search of the edit matrix.
  private int[] bdiag; 
  private int fdiagoff, bdiagoff;
  private final FileData[] filevec = new FileData[2];
  private int cost;
  
  public Diff(Object[] a, Object[] b) {
    Hashtable h = new Hashtable(a.length + b.length);
    filevec[0] = new FileData(a,h);
    filevec[1] = new FileData(b,h);
  }

  private int diag (int xoff, int xlim, int yoff, int ylim) {
    final int[] fd = fdiag;  // Give the compiler a chance.
    final int[] bd = bdiag;  // Additional help for the compiler.
    final int[] xv = xvec;    // Still more help for the compiler.
    final int[] yv = yvec;    // And more and more . . .
    final int dmin = xoff - ylim;  // Minimum valid diagonal.
    final int dmax = xlim - yoff;  // Maximum valid diagonal.
    final int fmid = xoff - yoff;  // Center diagonal of top-down search.
    final int bmid = xlim - ylim;  // Center diagonal of bottom-up search.
    int fmin = fmid, fmax = fmid;  // Limits of top-down search.
    int bmin = bmid, bmax = bmid;  // Limits of bottom-up search.
    
    // True if southeast corner is on an odd diagonal with respect to the northwest.
    final boolean odd = (fmid - bmid & 1) != 0;  

    fd[fdiagoff + fmid] = xoff;
    bd[bdiagoff + bmid] = xlim;

    for (int c = 1;; ++c) {
      int d;      // Active diagonal.
      boolean bigSnake = false;

      // Extend the top-down search by an edit step in each diagonal.
      if (fmin > dmin)
        fd[fdiagoff + --fmin - 1] = -1;
      else
        ++fmin;
      if (fmax < dmax)
        fd[fdiagoff + ++fmax + 1] = -1;
      else
        --fmax;
      for (d = fmax; d >= fmin; d -= 2) {
        int x, y, oldx, tlo = fd[fdiagoff + d - 1], thi = fd[fdiagoff + d + 1];
  
        if (tlo >= thi)
          x = tlo + 1;
        else
          x = thi;
        oldx = x;
        y = x - d;
        while (x < xlim && y < ylim && xv[x] == yv[y]) {
          ++x; ++y;
        }
        if (x - oldx > 20)
          bigSnake = true;
        fd[fdiagoff + d] = x;
        if (odd && bmin <= d && d <= bmax && bd[bdiagoff + d] <= fd[fdiagoff + d]) {
          cost = 2 * c - 1;
          return d;
        }
      }

      /* Similar extend the bottom-up search. */
      if (bmin > dmin)
        bd[bdiagoff + --bmin - 1] = Integer.MAX_VALUE;
      else
        ++bmin;
      if (bmax < dmax)
        bd[bdiagoff + ++bmax + 1] = Integer.MAX_VALUE;
      else
        --bmax;
      for (d = bmax; d >= bmin; d -= 2) {
        int x, y, oldx, tlo = bd[bdiagoff + d - 1], thi = bd[bdiagoff + d + 1];
  
        if (tlo < thi)
          x = tlo;
        else
          x = thi - 1;
        oldx = x;
        y = x - d;
        while (x > xoff && y > yoff && xv[x - 1] == yv[y - 1]) {
          --x; --y;
        }
        if (oldx - x > 20)
          bigSnake = true;
        bd[bdiagoff + d] = x;
        if (!odd && fmin <= d && d <= fmax && bd[bdiagoff + d] <= fd[fdiagoff + d]) {
          cost = 2 * c;
          return d;
        }
      }

      if (c > 200 && bigSnake && heuristic) {
          int best = 0;
          int bestpos = -1;
    
          for (d = fmax; d >= fmin; d -= 2) {
            int dd = d - fmid;
            if ((fd[fdiagoff + d] - xoff)*2 - dd > 12 * (c + (dd > 0 ? dd : -dd))) {
              if (fd[fdiagoff + d] * 2 - dd > best && fd[fdiagoff + d] - xoff > 20
                  && fd[fdiagoff + d] - d - yoff > 20) {
                int k;
                int x = fd[fdiagoff + d];
        
                /* We have a good enough best diagonal;
                   now insist that it end with a significant snake.  */
                for (k = 1; k <= 20; k++)
                  if (xvec[x - k] != yvec[x - d - k])
                    break;
          
                if (k == 21) {
                  best = fd[fdiagoff + d] * 2 - dd;
                  bestpos = d;
                }
              }
           }
          }
          if (best > 0) {
            cost = 2 * c - 1;
            return bestpos;
          }
    
          best = 0;
          for (d = bmax; d >= bmin; d -= 2) {
          int dd = d - bmid;
          if ((xlim - bd[bdiagoff + d])*2 + dd > 12 * (c + (dd > 0 ? dd : -dd))) {
            if ((xlim - bd[bdiagoff + d]) * 2 + dd > best  && xlim - bd[bdiagoff + d] > 20
                 && ylim - (bd[bdiagoff + d] - d) > 20) {
              // We have a good enough best diagonal; now insist that it end with a significant snake.
              int k;
              int x = bd[bdiagoff + d];
      
              for (k = 0; k < 20; k++)
                if (xvec[x + k] != yvec[x - d + k])
                  break;
              if (k == 20) {
                best = (xlim - bd[bdiagoff + d]) * 2 + dd;
                bestpos = d;
              }
            }
          }
        }
        if (best > 0) {
          cost = 2 * c - 1;
          return bestpos;
        }
      }
    }
  }

  private void compareseq (int xoff, int xlim, int yoff, int ylim) {
    /* Slide down the bottom initial diagonal. */
    while (xoff < xlim && yoff < ylim && xvec[xoff] == yvec[yoff]) {
      ++xoff; ++yoff;
    }
    /* Slide up the top initial diagonal. */
    while (xlim > xoff && ylim > yoff && xvec[xlim - 1] == yvec[ylim - 1]) {
      --xlim; --ylim;
    }
    
    /* Handle simple cases. */
    if (xoff == xlim)
      while (yoff < ylim)
        filevec[1].changedFlag[1+filevec[1].realindexes[yoff++]] = true;
    else if (yoff == ylim)
      while (xoff < xlim)
        filevec[0].changedFlag[1+filevec[0].realindexes[xoff++]] = true;
    else {
      /* Find a point of correspondence in the middle of the files.  */
      
      int d = diag (xoff, xlim, yoff, ylim);
      int c = cost;
      int f = fdiag[fdiagoff + d];
      int b = bdiag[bdiagoff + d];
      
      if (c == 1) {
        throw new IllegalArgumentException("Empty subsequence");
      } else {
        compareseq (xoff, b, yoff, b - d);
        compareseq (b, xlim, b - d, ylim);
      }
    }
  }

  /* Discard lines from one file that have no matches in the other file. */
  private void discard_confusing_lines() {
    filevec[0].discard_confusing_lines(filevec[1]);
    filevec[1].discard_confusing_lines(filevec[0]);
  }

  private boolean inhibit = false;

  /* Adjust inserts/deletes of blank lines to join changes as much as possible. */
  private void shiftBoundaries() {
    if (inhibit)
      return;
    filevec[0].shiftBoundaries(filevec[1]);
    filevec[1].shiftBoundaries(filevec[0]);
  }

  public interface ScriptBuilder {
  /* Scan the tables of which lines are inserted and deleted, producing an edit script. */
    public change buildScript(
      boolean[] changed0,int len0,
      boolean[] changed1,int len1
    );
  }

  /* Scan the tables of which lines are inserted and deleted, 
     producing an edit script in reverse order.  */
  static class ReverseScript implements ScriptBuilder {
    
    public  change buildScript(final boolean[] changed0, int len0,
                                final boolean[] changed1, int len1) {
      change script = null;
      int i0 = 0, i1 = 0;
      while (i0 < len0 || i1 < len1) {
        if (changed0[1+i0] || changed1[1+i1]) {
          int line0 = i0, line1 = i1;
    
          /* Find # lines changed here in each file.  */
          while (changed0[1+i0]) 
            ++i0;
          while (changed1[1+i1]) 
            ++i1;
    
          /* Record this change.  */
          script = new change(line0, line1, i0 - line0, i1 - line1, script);
        }

        /* We have reached lines in the two files that match each other.  */
        i0++; i1++;
      }
      return script;
    }
  }

  /* Scan the tables of which lines are inserted and deleted,
     producing an edit script in forward order.  */
  static class ForwardScript implements ScriptBuilder {

    public change buildScript(final boolean[] changed0, int len0,
                              final boolean[] changed1, int len1) {
      change script = null;
      int i0 = len0, i1 = len1;

      while (i0 >= 0 || i1 >= 0) {
        if (changed0[i0] || changed1[i1]) {
          int line0 = i0, line1 = i1;
  
          /* Find # lines changed here in each file.  */
          while (changed0[i0]) 
            --i0;
          while (changed1[i1]) 
            --i1;
  
          /* Record this change.  */
          script = new change(i0, i1, line0 - i0, line1 - i1, script);
        }
  
        /* We have reached lines in the two files that match each other.  */
        i0--; i1--;
      }
      return script;
    }
  }

  /* Standard ScriptBuilders. */
  public final static ScriptBuilder
    forwardScript = new ForwardScript(),
    reverseScript = new ReverseScript();

  /* Report the differences of two files.  DEPTH is the current directory depth. */
  public final change diff(final boolean reverse) {
    return diff(reverse ? reverseScript : forwardScript);
  }

  /* Get the results of comparison as an edit script.  The script 
     is described by a list of changes.  The standard ScriptBuilder
     implementations provide for forward and reverse edit scripts.
     Alternate implementations could, for instance, list common elements 
     instead of differences.
   */
  public change diff(final ScriptBuilder bld) {

    /* Some lines are obviously insertions or deletions
       because they don't match anything.  Detect them now,
       and avoid even thinking about them in the main comparison algorithm.  */
    discard_confusing_lines ();

    /* Now do the main comparison algorithm, considering just the
       undiscarded lines.  */

    xvec = filevec[0].undiscarded;
    yvec = filevec[1].undiscarded;

    int diags = filevec[0].nondiscarded_lines + filevec[1].nondiscarded_lines + 3;
    fdiag = new int[diags];
    fdiagoff = filevec[1].nondiscarded_lines + 1;
    bdiag = new int[diags];
    bdiagoff = filevec[1].nondiscarded_lines + 1;

    compareseq (0, filevec[0].nondiscarded_lines,
    0, filevec[1].nondiscarded_lines);
    fdiag = null;
    bdiag = null;

    /* Modify the results slightly to make them prettier
       in cases where that can validly be done.  */

    shiftBoundaries();

    /* Get the results of comparison in the form of a chain
       of `struct change's -- an edit script.  */
    return bld.buildScript(
      filevec[0].changedFlag,
      filevec[0].bufferedLines,
      filevec[1].changedFlag,
      filevec[1].bufferedLines
    );

  }

  /* The result of comparison is an "edit script": a chain of change objects.
     Each change represents one place where some lines are deleted
     and some are inserted.
     
     LINE0 and LINE1 are the first affected lines in the two files (origin 0).
     DELETED is the number of lines deleted here from file 0.
     INSERTED is the number of lines inserted here in file 1.

     If DELETED is 0 then LINE0 is the number of the line before
     which the insertion was done; vice versa for INSERTED and LINE1.  */

  public static class change {
    /* Previous or next edit command. */
    public change link;    
    /* # lines of file 1 changed here.  */
    public final int inserted;  
    /* # lines of file 0 changed here.  */
    public final int deleted;    
    /* Line number of 1st deleted line.  */
    public final int line0;    
    /* Line number of 1st inserted line.  */
    public final int line1;    

    /* Cons an additional entry onto the front of an edit script OLD.
       LINE0 and LINE1 are the first affected lines in the two files (origin 0).
       DELETED is the number of lines deleted here from file 0.
       INSERTED is the number of lines inserted here in file 1.

       If DELETED is 0 then LINE0 is the number of the line before
       which the insertion was done; vice versa for INSERTED and LINE1.  */
    public change(int line0, int line1, int deleted, int inserted, change old) {
      this.line0 = line0;
      this.line1 = line1;
      this.inserted = inserted;
      this.deleted = deleted;
      this.link = old;
    }
  }

  /* Data on one input file being compared. */
  class FileData {
    
    /* Number of elements (lines) in this file. */
    final int bufferedLines;
  
    /* Vector, indexed by line number, containing an equivalence code for
       each line.  It is this vector that is actually compared with that
       of another file to generate differences. */
    private final int[] equivs;
  
    /* Vector, like the previous one except that
       the elements for discarded lines have been squeezed out.  */
    final int[]  undiscarded;
  
    /* Vector mapping virtual line numbers (not counting discarded lines)
       to real ones (counting those lines).  Both are origin-0.  */
    final int[]  realindexes;
  
    /* Total number of nondiscarded lines. */
    int nondiscarded_lines;
  
    /* Array, indexed by real origin-1 line number,
       containing true for a line that is an insertion or a deletion.
       The results of comparison are stored here.  */
    boolean[] changedFlag;

    /* Allocate changed array for the results of comparison.  */
    void clear() {
      /* Allocate a flag for each line of each file, saying whether that line
         is an insertion or deletion. Allocate an extra element, always zero, 
         at each end of each vector. */
      changedFlag = new boolean[bufferedLines + 2];
    }

    /* Return equiv_count[I] as the number of lines in this file
       that fall in equivalence class I. */
    int[] equivCount() {
      int[] equiv_count = new int[equivMax];
      for (int i = 0; i < bufferedLines; ++i)
        ++equiv_count[equivs[i]];
      return equiv_count;
    }

    /* Discard lines that have no matches in another file.

       A line which is discarded will not be considered by the actual
       comparison algorithm; it will be as if that line were not in the file.
       The file's `realindexes' table maps virtual line numbers
       (which don't count the discarded lines) into real line numbers;
       this is how the actual comparison algorithm produces results
       that are comprehensible when the discarded lines are counted.
       When we discard a line, we also mark it as a deletion or insertion
       so that it will be printed in the output.  
     */
    void discard_confusing_lines(FileData f) {
      clear();
      /* Set up table of which lines are going to be discarded. */
      final byte[] discarded = discardable(f.equivCount());

      /* Don't really discard the provisional lines except when they occur
       in a run of discardables, with nonprovisionals at the beginning
       and end.  */
      filterDiscards(discarded);

      /* Actually discard the lines. */
      discard(discarded);
    }

    /* Mark to be discarded each line that matches no line of another file.
       If a line matches many lines, mark it as provisionally discardable. for each line
     */
    private byte[] discardable(final int[] counts) {
      final int end = bufferedLines;
      final byte[] discards = new byte[end];
      final int[] equivs = this.equivs;
      int many = 5;
      int tem = end / 64;

      /* Multiply MANY by approximate square root of number of lines.
         That is the threshold for provisionally discardable lines.  */
      while ((tem = tem >> 2) > 0)
        many *= 2;

      for (int i = 0; i < end; i++) {
        int nmatch;
        if (equivs[i] == 0)
          continue;
        nmatch = counts[equivs[i]];
        if (nmatch == 0)
          discards[i] = 1;
        else if (nmatch > many)
          discards[i] = 2;
      }
      return discards;
    }

    /* Don't really discard the provisional lines except when they occur
       in a run of discardables, with nonprovisionals at the beginning
       and end.  */

    private void filterDiscards(final byte[] discards) {
      final int end = bufferedLines;

      for (int i = 0; i < end; i++) {
        /* Cancel provisional discards not in middle of run of discards.  */
        if (discards[i] == 2)
          discards[i] = 0;
        else if (discards[i] != 0) {
          /* We have found a nonprovisional discard.  */
          int j;
          int length;
          int provisional = 0;
      
          /* Find end of this run of discardable lines.
             Count how many are provisionally discardable.  */
          for (j = i; j < end; j++) {
            if (discards[j] == 0)
              break;
            if (discards[j] == 2)
              ++provisional;
          }
      
          /* Cancel provisional discards at end, and shrink the run.  */
          while (j > i && discards[j - 1] == 2) {
            discards[--j] = 0; --provisional;
          }
      
          /* Now we have the length of a run of discardable lines
             whose first and last are not provisional.  */
          length = j - i;
      
          /* If 1/4 of the lines in the run are provisional,
             cancel discarding of all provisional lines in the run.  */
          if (provisional * 4 > length) {
            while (j > i)
              if (discards[--j] == 2)
                discards[j] = 0;
          } else {
            int consec;
            int minimum = 1;
            int tem = length / 4;
      
            /* MINIMUM is approximate square root of LENGTH/4.
               A subrun of two or more provisionals can stand
               when LENGTH is at least 16.
               A subrun of 4 or more can stand when LENGTH >= 64.  */
            while ((tem = tem >> 2) > 0)
              minimum *= 2;
            minimum++;
      
            /* Cancel any subrun of MINIMUM or more provisionals
               within the larger run.  */
            for (j = 0, consec = 0; j < length; j++)
              if (discards[i + j] != 2)
                consec = 0;
            else if (minimum == ++consec)
              /* Back up to start of subrun, to cancel it all.  */
              j -= consec;
            else if (minimum < consec)
              discards[i + j] = 0;
      
            /* Scan from beginning of run
               until we find 3 or more nonprovisionals in a row
               or until the first nonprovisional at least 8 lines in.
               Until that point, cancel any provisionals.  */
            for (j = 0, consec = 0; j < length; j++) {
              if (j >= 8 && discards[i + j] == 1)
                break;
              if (discards[i + j] == 2) {
                consec = 0; discards[i + j] = 0;
              } else if (discards[i + j] == 0)
                consec = 0;
              else
                consec++;
              if (consec == 3)
                break;
            }
      
            /* I advances to the last line of the run.  */
            i += length - 1;
        
            /* Same thing, from end.  */
            for (j = 0, consec = 0; j < length; j++) {
              if (j >= 8 && discards[i - j] == 1)
                break;
              if (discards[i - j] == 2) {
                consec = 0; discards[i - j] = 0;
              } else if (discards[i - j] == 0)
                consec = 0;
              else
                consec++;
              if (consec == 3)
                break;
            }
          }
        }
      }
    }

    /* Actually discard the lines. */
    private void discard(final byte[] discards) {
      final int end = bufferedLines;
      int j = 0;
      for (int i = 0; i < end; ++i)
        if (noDiscards || discards[i] == 0) {
          undiscarded[j] = equivs[i];
          realindexes[j++] = i;
        } else
          changedFlag[1+i] = true;
        nondiscarded_lines = j;
      }
  
      FileData(Object[] data,Hashtable h) {
        bufferedLines = data.length;
  
        equivs = new int[bufferedLines]; 
        undiscarded = new int[bufferedLines];
        realindexes = new int[bufferedLines];
  
        for (int i = 0; i < data.length; ++i) {
          Integer ir = (Integer)h.get(data[i]);
          if (ir == null)
            h.put(data[i],new Integer(equivs[i] = equivMax++));
          else
            equivs[i] = ir.intValue();
        }
      }
  
      /* Adjust inserts/deletes of blank lines to join changes
         as much as possible.
  
         We do something when a run of changed lines include a blank
         line at one end and have an excluded blank line at the other.
         We are free to choose which blank line is included.
         `compareseq' always chooses the one at the beginning,
         but usually it is cleaner to consider the following blank line
         to be the "change".  The only exception is if the preceding blank line
         would join this change to other changes.  
      */
  
      void shiftBoundaries(FileData f) {
        final boolean[] changed = changedFlag;
        final boolean[] other_changed = f.changedFlag;
        int i = 0;
        int j = 0;
        int i_end = bufferedLines;
        int preceding = -1;
        int other_preceding = -1;
  
        for (;;) {
          int start, end, other_start;
  
        /* Scan forwards to find beginning of another run of changes.
           Also keep track of the corresponding point in the other file.  */
    
        while (i < i_end && !changed[1+i]) {
          while (other_changed[1+j++])
            /* Non-corresponding lines in the other file
               will count as the preceding batch of changes.  */
            other_preceding = j;
          i++;
        }
    
        if (i == i_end)
          break;
    
        start = i;
        other_start = j;
    
        for (;;) {
          /* Now find the end of this run of changes.  */
    
          while (i < i_end && changed[1+i]) 
            i++;
          end = i;
    
          /* If the first changed line matches the following unchanged one,
            and this run does not follow right after a previous run,
            and there are no lines deleted from the other file here,
            then classify the first changed line as unchanged
            and the following line as changed in its place.  */
    
           /* You might ask, how could this run follow right after another?
              Only because the previous run was shifted here.  */
    
           if (end != i_end && equivs[start] == equivs[end] && !other_changed[1+j]
               && end != i_end && !((preceding >= 0 && start == preceding)
               || (other_preceding >= 0
               && other_start == other_preceding))) {
            changed[1+end++] = true;
            changed[1+start++] = false;
            ++i;
            /* Since one line-that-matches is now before this run
               instead of after, we must advance in the other file
               to keep in synch.  */
            ++j;
          } else
            break;
        }
        preceding = i;
        other_preceding = j;
      }
    }
  }
}
