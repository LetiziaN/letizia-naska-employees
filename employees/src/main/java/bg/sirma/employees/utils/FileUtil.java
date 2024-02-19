package bg.sirma.employees.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

public final class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	private FileUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Validates the uploaded file based on magic match and specified criteria.
     *
     * @param uploadedFileByteArray bytes of the file to validate
     * @param maxFileSize           the maximum file size
     * @param fileSizeMultiplier    file size multiplier
     * @param allowedMimeTypes      allowed MIME types separated by commas
     * @return the validation result
     * @throws Exception                   if one of the validations fails
     * @throws MagicParseException         thrown by the MagicMatch
     * @throws MagicMatchNotFoundException thrown by the MagicMatch
     * @throws MagicException              thrown by the MagicMatch
     */
    public static MagicMatch validateFile(byte[] uploadedFileByteArray, Integer maxFileSize, int fileSizeMultiplier,
            String allowedMimeTypes) throws Exception {

        MagicMatch match = getMagicMatch(uploadedFileByteArray);

        validateExtension(match);
        validateFileSize(uploadedFileByteArray, maxFileSize, fileSizeMultiplier);

        return match;
    }

    private static MagicMatch getMagicMatch(byte[] uploadedFileByteArray)
            throws MagicParseException, MagicMatchNotFoundException, MagicException {
        return Magic.getMagicMatch(uploadedFileByteArray, true);
    }

    private static void validateExtension(MagicMatch match) throws Exception {
        if (match.getExtension() == null) {
            throw new Exception("File extension not present");
        }
    }

    private static void validateFileSize(byte[] uploadedFileByteArray, Integer maxFileSize, int fileSizeMultiplier)
            throws Exception {
        if (maxFileSize != null && uploadedFileByteArray.length < (maxFileSize * fileSizeMultiplier)) {
            logger.debug("Size of the file {} is less than max file size {}", uploadedFileByteArray.length,
                    maxFileSize * fileSizeMultiplier);
        } else {
            throw new Exception("File max size exceeded");
        }
    }

}