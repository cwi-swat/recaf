module lang::recaf::desugar::Recaffeinate

import lang::recaf::desugar::RecafStmts;
import lang::recaf::desugar::RecafFull;
import lang::recaf::Recaf;
import List;
import Set;
import String;
import IO;  
import util::Maybe;

list[Id] collectAlgFields(start[CompilationUnit] cu) {
   list[Id] algFields = [];
   visit (cu) {
      case (FieldDec)`<BeforeField* bf1> recaf <BeforeField* bf2> <Type t> <Id x> = <Expr e>;`:
        algFields += [x];
      case (FieldDec)`<BeforeField* bf1> recaf <BeforeField* bf2> <Type t> <Id x>;`:
        algFields += [x];
   }
   return algFields;
}

start[CompilationUnit] recaffeinate(start[CompilationUnit] cu) {
   algFields = collectAlgFields(cu); 

   return top-down visit (cu) {
      case (FieldDec)`<BeforeField* bf1> recaf <BeforeField* bf2> <Type t> <Id x> = <Expr e>;`
        => (FieldDec)`<BeforeField* bf1> <BeforeField* bf2> <Type t> <Id x> = <Expr e>;`

      case (FieldDec)`<BeforeField* bf0> recaf <BeforeField* bf3> <Type t> <Id x>;`
        => (FieldDec)`<BeforeField* bf0> <BeforeField* bf3> <Type t> <Id x>;`
      
      case m:(MethodDec)`<BeforeMethod* _> <TypeParams? _> <ResultType _> <Id _>(recaf <ClassOrInterfaceType _> <Id _>, <{FormalParam ","}* _>) <Block _>`
        => recafStms(m, [])
      
      case m:(MethodDec)`<BeforeMethod* _> recaf <BeforeMethod* _> <TypeParams? _> <ResultType _> <Id _>(<{FormalParam ","}* _>) <Block _>`
        => recafStms(m, algFields) 

      case m:(MethodDec)`<BeforeMethod* _> <TypeParams? _> <ResultType _> <Id _>(recaff <ClassOrInterfaceType _> <Id _>, <{FormalParam ","}* _>) <Block _>`
        => recafFull(m, [])
      
      case m:(MethodDec)`<BeforeMethod* _> recaff <BeforeMethod* _> <TypeParams? _> <ResultType _> <Id _>(<{FormalParam ","}* _>) <Block _>`
        => recafFull(m, algFields) 
      
   }
}