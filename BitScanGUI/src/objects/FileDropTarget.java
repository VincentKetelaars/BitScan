package objects;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

import constants.GeneralMethods;

public class FileDropTarget extends DropTargetAdapter {
	private MainLogic mainLogic;

	public FileDropTarget(MainLogic ml, Component c) {
		mainLogic = ml;
		new DropTarget(c, DnDConstants.ACTION_COPY, this, true, null);
	}

	public synchronized void drop(DropTargetDropEvent evt) {
		try {
			evt.acceptDrop(DnDConstants.ACTION_COPY);
			List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

			if (droppedFiles.size() > 1) {
				GeneralMethods.showTooManyFilesErrorDialog(mainLogic.mainFrame());
				return;
			}

			File file = droppedFiles.get(0);
			String filename = file.getName();
			String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
			if (!file.isFile() || !GeneralMethods.isValidExtension(extension)) {
				GeneralMethods.showWrongFileErrorDialog(mainLogic.mainFrame());	
				return;
			}

			mainLogic.runFileReader(file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
