package dijkstra;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * �����ñ���ͼƬ��JPanel���ṩ��������ʾ����ͼƬ�ķ�ʽ�����С�ƽ�̺����졣
 * δ���ñ���ͼƬ������£�ͬJPanel��
 * a JPanel can set background which provide 3 method to display background��center tile and stretch
 * if there's no background ,it's the same as JPanel��
 * 
 * @author 003
 */
public class JImagePane extends JPanel
{
    private static final long serialVersionUID = -8251916094895167058L;
    
    /**
     *center ����
     */
    public static final String CENTRE = "Centre";
    
    /**
     * tile ƽ��
     */
    public static final String TILED = "Tiled";

    /**
     * stretch ����
     */
    public static final String SCALED = "Scaled";

    /**
     *  background ����ͼƬ
     */
    private Image backgroundImage;
    
    /**
     * display mode of background ����ͼƬ��ʾģʽ
     */
    private String imageDisplayMode;

    /**
     * The background display the index of display mode,which can provide a way to stretch
     * ����ͼƬ��ʾģʽ��������������������ڱ�Ҫʱ��չ��
     */
    private int modeIndex;

    /**
     * ����һ��û�б���ͼƬ��JImagePane
     * build a JImagePane with no background
     */
    public JImagePane()
    {
        this(null, CENTRE);
    }
    
    /**
     * build JImagrPane of a fixed background and display made
     * @param image background
     * @param modeName display mode of background
     * ����һ������ָ������ͼƬ��ָ����ʾģʽ��JImagePane
     * @param image ����ͼƬ
     * @param modeName ����ͼƬ��ʾģʽ
     */
    public JImagePane(Image image, String modeName)
    {
        super();
        setBackgroundImage(image);
        setImageDisplayMode(modeName);
    }
    
    /**
     * set the background
     * @param image background
     * ���ñ���ͼƬ
     * @param image ����ͼƬ
     */
    public void setBackgroundImage(Image image)
    {
        this.backgroundImage = image;
        this.repaint();
    }

    /**
     * obtain the background
     * @return background
     * ��ȡ����ͼƬ
     * @return ����ͼƬ
     */
    public Image getBackgroundImage()
    {
        return backgroundImage;
    }

    /**
     * set the display mode of background
     * @param modeName name of mode��limit to ImagePane.TILED  ImagePane.SCALED  ImagePane.CENTREED  ImagePane.SCALED  ImagePane.CENTRE
     * ���ñ���ͼƬ��ʾģʽ
     * @param modeName ģʽ���ƣ�ȡֵ������ImagePane.TILED  ImagePane.SCALED  ImagePane.CENTRE
     */
    public void setImageDisplayMode(String modeName)
    {
        if(modeName != null)
        {
            modeName = modeName.trim();
            
            //����
            if(modeName.equalsIgnoreCase(CENTRE))
            {
                this.imageDisplayMode = CENTRE;
                modeIndex = 0;
            }
            //ƽ��
            else if(modeName.equalsIgnoreCase(TILED))
            {
                this.imageDisplayMode = TILED;
                modeIndex = 1;
            }
            //����
            else if(modeName.equalsIgnoreCase(SCALED))
            {
                this.imageDisplayMode = SCALED;
                modeIndex = 2;
            }
            
            this.repaint();
        }
    }

    /**
     * obtain the display mode of background
     * @return Display mode
     * ��ȡ����ͼƬ��ʾģʽ
     * @return ��ʾģʽ
     */
    public String getImageDisplayMode()
    {
        return imageDisplayMode;
    }

    /**
     * draw component
     * @see javax.swing.JComponent#paintComponent(Graphics)
     * �������
     * @see javax.swing.JComponent#paintComponent(Graphics)
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        //��������˱���ͼƬ����ʾ
        if(backgroundImage != null)
        {
            int width = this.getWidth();
            int height = this.getHeight();
            int imageWidth = backgroundImage.getWidth(this);
            int imageHeight = backgroundImage.getHeight(this);

            switch(modeIndex)
            {
                //����
                case 0:
                {
                    int x = (width - imageWidth) / 2;
                    int y = (height - imageHeight) / 2;
                    g.drawImage(backgroundImage, x, y, this);
                    break;
                }
                //ƽ��
                case 1:
                {
                    for(int ix = 0; ix < width; ix += imageWidth)
                    {
                        for(int iy = 0; iy < height; iy += imageHeight)
                        {
                            g.drawImage(backgroundImage, ix, iy, this);
                        }
                    }
                    
                    break;
                }
                //����
                case 2:
                {
                    g.drawImage(backgroundImage, 0, 0, width, height, this);
                    break;
                }
            }
        }
    }
    

}

